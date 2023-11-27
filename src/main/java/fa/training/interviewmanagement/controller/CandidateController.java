package fa.training.interviewmanagement.controller;

import fa.training.interviewmanagement.entities.Candidate;
import fa.training.interviewmanagement.entities.Skill;
import fa.training.interviewmanagement.entities.SkillCandidate;
import fa.training.interviewmanagement.entities.Users;
import fa.training.interviewmanagement.repository.CandidateRepository;
import fa.training.interviewmanagement.repository.SkillRepository;
import fa.training.interviewmanagement.repository.UserRepository;
import fa.training.interviewmanagement.service.CandidateService;
import fa.training.interviewmanagement.service.FileService;
import fa.training.interviewmanagement.service.SkillCandidateService;
import fa.training.interviewmanagement.utils.SessionUtil;
import fa.training.interviewmanagement.web.request.CandidateRequest;
import fa.training.interviewmanagement.web.request.CandidateSearch;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@MultipartConfig(maxFileSize = 10000000)
public class CandidateController {

    final private CandidateService candidateService;

    final private FileService fileService;
    final private SkillRepository skillRepository;
    final private UserRepository userRepository;
    final private ModelMapper modelMapper;
    private final SkillCandidateService skillCandidateService;
    private final SessionUtil sessionUtil;

    @GetMapping("/candidate")
    public String getAllCandidate(Model model,
                                  HttpServletRequest req,
                                  @ModelAttribute CandidateSearch candidateSearch) {
        sessionUtil.setTitle(req,"CANDIDATE");
        candidateSearch = checkInputCandidateSearch(candidateSearch);
        Page<Candidate> candidates = candidateService.findAllCandidate(candidateSearch);
        int totalPage = candidates.getTotalPages();
        if (totalPage > 0) {
            List<Integer> integers = new ArrayList<>();
            for (int i = 0; i < totalPage; i++) {
                integers.add(i);
            }
            candidateSearch.setPageMaxNumbers(integers);
        } else {
            candidateSearch.setMessage("Candidate is not found!");
        }
        System.out.println(candidateSearch.getMessage());
        model.addAttribute("candidates", candidates);
        model.addAttribute("candidateSearch", candidateSearch);
        return "ui/Candidate/CandidateListView";
    }


    @GetMapping("/candidate/create")
    public String getCreateCandidate(@ModelAttribute CandidateRequest candidateRequest,
                                     Model model) {
        List<Skill> skills = skillRepository.findAll();
        List<Users> recruiters = userRepository.findAll();
        model.addAttribute("skills", skills);
        model.addAttribute("recruiters", recruiters);
        return "ui/Candidate/CandidateListAdd";
    }

    @GetMapping("/candidate/edit/{id}")
    public String getEditCandidate(Model model,
                                   @ModelAttribute CandidateRequest candidateRequest,
                                   @PathVariable Long id) {
        Candidate candidate = candidateService.findCandidateById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found this candidate detail!"));
        List<Skill> skills = skillRepository.findAll();
        List<Users> recruiters = userRepository.findAll();
        List<Long> skillIds = candidate.getSkillCandidates().stream().map(x -> x.getSkill().getId()).toList();
        model.addAttribute("recruiters", recruiters);
        model.addAttribute("skillIds", skillIds);
        model.addAttribute("skills", skills);
        model.addAttribute("candidate", candidate);
        return "ui/Candidate/CandidateListEdit";
    }

    @GetMapping("/candidate/delete/{id}")
    public String deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return "redirect:/candidate";
    }

    @PostMapping("/candidate/create")
    public String createCandidate(@ModelAttribute @Validated CandidateRequest candidateRequest,
                                  BindingResult result,
                                  RedirectAttributes ra) throws IOException {
        if(result.hasErrors()){
            return "ui/Candidate/CandidateListAdd";
        }
        Candidate candidate = modelMapper.map(candidateRequest, Candidate.class);
        Users recruiter = userRepository.findById(candidateRequest.getRecruiterId()).orElseThrow();
        List<Skill> skills = skillRepository.findAllById(candidateRequest.getSkills());
        List<SkillCandidate> skillCandidates = skills.stream().map(
                s -> SkillCandidate.builder().skill(s).candidate(candidate).build()
        ).toList();
        candidate.setCv(candidateRequest.getCv().getOriginalFilename());
        candidate.setSkillCandidates(skillCandidates);
        candidate.setRecruiter(recruiter);
        Candidate candidateSave = candidateService.saveCandidate(candidate);
        fileService.saveFile(candidateRequest.getCv(), candidateSave.getId());
        ra.addFlashAttribute("message", "success");
        return "redirect:/candidate/create";
    }


    @GetMapping("/candidate/{id}")
    public String candidateDetail(@PathVariable Long id, Model model) {
        Candidate candidate = candidateService.findCandidateById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found this candidate detail!"));

        model.addAttribute("candidate", candidate);
        return "ui/Candidate/CandidateListDetail";
    }

    @PostMapping("/candidate/edit/{id}")
    public String editCandidate(RedirectAttributes ra,
                                @PathVariable Long id,
                                Model model,
                                @ModelAttribute @Validated CandidateRequest candidateRequest,
                                BindingResult result) throws IOException {
        if (result.hasErrors()){
            return getEditCandidate(model, candidateRequest, id);
        }
        skillCandidateService.deleteByCandidateId(id);
        Candidate candidate = candidateService.findCandidateById(id).orElseThrow();
        modelMapper.map(candidateRequest, candidate);
        Users recruiter = userRepository.findById(candidateRequest.getRecruiterId()).orElseThrow();
        List<Skill> skills = skillRepository.findAllById(candidateRequest.getSkills());
        List<SkillCandidate> skillCandidates = new ArrayList<>();
        for (Skill s : skills) {
            skillCandidates.add(SkillCandidate.builder().skill(s).candidate(candidate).build());
        }
        System.out.println(candidateRequest.getCv());
        if (candidateRequest.getCv() != null && !candidateRequest.getCv().isEmpty()) {
            fileService.saveFile(candidateRequest.getCv(), candidateRequest.getId());
            candidate.setCv(candidateRequest.getCv().getOriginalFilename());
        }
        candidate.setSkillCandidates(skillCandidates);
        candidate.setRecruiter(recruiter);
        Candidate candidateSave = candidateService.saveCandidate(candidate);
        ra.addFlashAttribute("candidate", candidateSave);
        ra.addFlashAttribute("message", "Edit successfull");
        return "redirect:/candidate/edit/" + id;
    }

    @GetMapping("/candidate/download/{id}")
    public void download(HttpServletResponse resp, @PathVariable Long id) {
        resp.setContentType("application/octet-stream");
        Candidate candidate = candidateService.findCandidateById(id).orElseThrow();
        String keyHeader = "Content-Disposition";
        String valueHeader = "attachment; filename=" + candidate.getCv();
        resp.setHeader(keyHeader, valueHeader);
        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            outputStream.write(fileService.downloadFile(candidate.getId(), candidate.getCv()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CandidateSearch checkInputCandidateSearch(CandidateSearch candidateSearch) {
        if (candidateSearch.getPageNumber() == null) {
            candidateSearch.setPageNumber(1);
        }
        if (candidateSearch.getNameKeyword() == null || candidateSearch.getNameKeyword().isEmpty()) {
            candidateSearch.setNameKeyword("");
        }
        return candidateSearch;
    }
}
