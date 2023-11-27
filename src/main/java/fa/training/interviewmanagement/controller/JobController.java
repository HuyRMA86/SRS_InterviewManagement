package fa.training.interviewmanagement.controller;

import fa.training.interviewmanagement.entities.*;
import fa.training.interviewmanagement.repository.BenefitsRepository;
import fa.training.interviewmanagement.repository.LevelRepository;
import fa.training.interviewmanagement.repository.SkillRepository;
import fa.training.interviewmanagement.service.FileService;
import fa.training.interviewmanagement.service.JobService;
import fa.training.interviewmanagement.web.request.JobImportFile;
import fa.training.interviewmanagement.web.request.JobRequest;
import fa.training.interviewmanagement.web.request.JobSave;
import fa.training.interviewmanagement.web.request.JobSearch;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/job")
@MultipartConfig
public class JobController {

    private final JobService jobService;
    private final SkillRepository skillRepository;
    private final LevelRepository levelRepository;
    private final BenefitsRepository benefitsRepository;
    private final ModelMapper modelMapper;
    private final FileService<JobImportFile> fileService;
    @GetMapping("")
    public String jobList(HttpServletRequest req, Model model, @ModelAttribute JobSearch jobSearch) {
        setTitle(req);
        jobSearch = checkJobSearch(jobSearch);

        Page<Job> jobs = jobService.findAllJob(jobSearch);
        int totalPage = jobs.getTotalPages();

        if (totalPage > 0) {
            List<Integer> integers = new ArrayList<>();
            for (int i = 1; i <= totalPage; i++) {
                integers.add(i);
            }
            jobSearch.setPageNumbers(integers);
        } else {
            jobSearch.setMessage("No job results found!!!");
        }

        model.addAttribute("jobs", jobs);
        model.addAttribute("jobSearch", jobSearch);
        return "ui/job/job-list";
    }

    @GetMapping("/delete/{id}")
    public String jobDelete(@PathVariable Long id) {
        jobService.deleteJobById(id);
        return "redirect:/job";
    }

    @GetMapping("/add")
    public String jobCreate(@ModelAttribute("jobRequest") JobRequest jobRequest, Model model,
                            HttpServletRequest req) {
        HttpSession session = req.getSession();
        model.addAttribute("skills", session.getAttribute("skills"));
        model.addAttribute("levels", session.getAttribute("levels"));
        model.addAttribute("benefits", session.getAttribute("benefits"));
        return "ui/job/job-create";
    }

    @PostMapping("/add")
    public String jobCreateForm(@ModelAttribute("jobRequest") @Validated JobRequest jobRequest, BindingResult bindingResult,
                                HttpServletRequest req, RedirectAttributes ra, Model model) {
        if (bindingResult.hasErrors()) {
            HttpSession session = req.getSession();
            model.addAttribute("skills", session.getAttribute("skills"));
            model.addAttribute("levels", session.getAttribute("levels"));
            model.addAttribute("benefits", session.getAttribute("benefits"));
            return "ui/job/job-create";
        }

        List<JobSave> jobSaves = jobService.checkValidator(jobRequest);
        if(!jobSaves.isEmpty()){
            ra.addFlashAttribute("jobSaves", jobSaves);
            return "redirect:/job/add";
        }

        Job job = jobService.createJobByJobRequest(jobRequest);
        JobSave jobSave = jobService.saveJob(job);
        ra.addFlashAttribute("jobSave", jobSave);
        return "redirect:/job/add";
    }

    @GetMapping("/edit/{id}")
    public String jobUpdate(@PathVariable Long id, Model model, @RequestParam(value = "subModule", required = false) String subModule,
                            HttpServletRequest req) {
        Job job = jobService.findJobById(id);
        Long levelId = job.getLevel().getId();
        List<SkillJob> skillJobs = job.getSkillJobs();
        List<Long> skillsId = skillJobs.stream().map(skillJob -> skillJob.getSkill().getId()).toList();
        List<BenefitsJob> benefitsJobs = job.getBenefitsJobs();
        List<Long> benefitsId = benefitsJobs.stream().map(benefitsJob -> benefitsJob.getBenefits().getId()).toList();
        job.setBenefitsJobs(null);
        job.setSkillJobs(null);
        job.setLevel(null);
        JobRequest jobRequest = modelMapper.map(job, JobRequest.class);
        jobRequest.setLevel(levelId);
        jobRequest.setBenefits(benefitsId);
        jobRequest.setSkills(skillsId);

        HttpSession session = req.getSession();
        model.addAttribute("skills", session.getAttribute("skills"));
        model.addAttribute("levels", session.getAttribute("levels"));
        model.addAttribute("benefits", session.getAttribute("benefits"));
        model.addAttribute("jobRequest", jobRequest);
        model.addAttribute("subModule", subModule);
        return "ui/job/job-update";
    }

    @PostMapping("/edit")
    public String jobUpdateForm(@ModelAttribute("jobRequest") @Validated JobRequest jobRequest, BindingResult bindingResult,
                                HttpServletRequest req, RedirectAttributes ra, @RequestParam("subModule") String subModule, Model model) {
        List<SkillJob> skillJobs = jobService.findJobById(jobRequest.getId()).getSkillJobs();
        List<Long> skillIdOlds = skillJobs.stream().map(x -> x.getSkill().getId()).toList();

        List<BenefitsJob> benefitsJobs = jobService.findJobById(jobRequest.getId()).getBenefitsJobs();
        List<Long> benefitIdOlds = benefitsJobs.stream().map(x -> x.getBenefits().getId()).toList();

        List<Long> skillIds = jobRequest.getSkills();
        List<Long> benefitIds = jobRequest.getBenefits();

        if (bindingResult.hasErrors()) {
            HttpSession session = req.getSession();
            model.addAttribute("skills", session.getAttribute("skills"));
            model.addAttribute("levels", session.getAttribute("levels"));
            model.addAttribute("benefits", session.getAttribute("benefits"));
            return "ui/job/job-update";
        }

        List<JobSave> jobSaves = jobService.checkValidator(jobRequest);
        if(!jobSaves.isEmpty()){
            ra.addFlashAttribute("jobSaves", jobSaves);
            ra.addAttribute("id", jobRequest.getId());
            ra.addAttribute("subModule", subModule);
            return "redirect:/job/edit/{id}";
        }
        Job job = jobService.createJobByJobRequest(jobRequest);
        JobSave jobSave = jobService.updateJob(job);

        jobService.deleteSkills(skillIdOlds, skillIds, jobRequest.getId());
        jobService.deleteBenefits(benefitIdOlds, benefitIds, jobRequest.getId());

        ra.addFlashAttribute("jobSave", jobSave);
        ra.addAttribute("id", jobRequest.getId());
        ra.addAttribute("subModule", subModule);
        return "redirect:/job/edit/{id}";
    }

    @GetMapping("/detail/{id}")
    public String jobDetail(@PathVariable Long id, Model model) {
        Job job = jobService.findJobById(id);
        model.addAttribute("job", job);
        return "ui/job/job-detail";
    }

    @PostMapping("/import")
    public String importJobByFileExcel(@RequestParam MultipartFile fileExcel, RedirectAttributes ra){
        List<JobImportFile> jobImportFiles = fileService.importExcel(fileExcel, JobImportFile.class);
        List<Job> jobs = jobService.jobsByImport(jobImportFiles);
        JobSave jobSave = jobService.saveAllJob(jobs);

        ra.addFlashAttribute("jobSave", jobSave);
        return "redirect:/job";
    }

    public JobSearch checkJobSearch(JobSearch jobSearch) {
        if (jobSearch.getPageIndex() == null) {
            jobSearch.setPageIndex(1);
        }
        if (jobSearch.getNameSearch() == null || jobSearch.getNameSearch().isEmpty()) {
            jobSearch.setNameSearch("");
        }
        return jobSearch;
    }

    private void setTitle(HttpServletRequest req) {
        HttpSession httpSession = req.getSession();
        List<Skill> skills = skillRepository.findAll();
        List<Level> levels = levelRepository.findAll();
        List<Benefits> benefits = benefitsRepository.findAll();

        httpSession.setAttribute("title", "JOB");
        httpSession.setAttribute("skills", skills);
        httpSession.setAttribute("levels", levels);
        httpSession.setAttribute("benefits", benefits);
    }


}
