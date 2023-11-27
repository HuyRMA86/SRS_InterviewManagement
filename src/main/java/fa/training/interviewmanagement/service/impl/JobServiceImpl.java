package fa.training.interviewmanagement.service.impl;

import fa.training.interviewmanagement.entities.*;
import fa.training.interviewmanagement.enums.EStatus;
import fa.training.interviewmanagement.repository.*;
import fa.training.interviewmanagement.service.JobService;
import fa.training.interviewmanagement.web.request.JobImportFile;
import fa.training.interviewmanagement.web.request.JobRequest;
import fa.training.interviewmanagement.web.request.JobSave;
import fa.training.interviewmanagement.web.request.JobSearch;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;
    private final LevelRepository levelRepository;
    private final SkillRepository skillRepository;
    private final BenefitsRepository benefitsRepository;
    private final SkillJobRepository skillJobRepository;
    private final BenefitsJobRepository benefitsJobRepository;

    @Override
    public Page<Job> findAllJob(JobSearch jobSearch) {
        Pageable pageable = PageRequest.of(jobSearch.getPageIndex() - 1, 4);
        Page<Job> jobs = null;
        if (jobSearch.getStatus() == null) {
            jobs = jobRepository.getAllJob(jobSearch.getNameSearch(), pageable);
        } else {
            jobs = jobRepository.getAllJob(jobSearch.getNameSearch(), jobSearch.getStatus(), pageable);
        }
        return jobs;
    }

    @Override
    public Job findJobById(Long id) {
        return jobRepository.findById(id).orElseThrow();
    }

    @Transactional
    @Override
    public void deleteJobById(Long id) {
        if (findJobById(id) != null) {
            jobRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    @Transactional
    public JobSave saveJob(Job job) {
        JobSave jobSave = new JobSave();
        List<Job> jobs = jobRepository.findJobByTitleAndLevel(job.getTitle(), job.getLevel());
        List<Job> jobs1 = jobs.stream().filter(job1 -> !job1.getStatus().equals(EStatus.CLOSE)).toList();
        if (jobs.isEmpty() || jobs1.isEmpty()) {
            jobRepository.save(job);
            jobSave.setStatus("Success");
            jobSave.setMessage("Save job success!!!");
        } else {
            jobSave.setStatus("Fail");
            jobSave.setMessage("Save job fail!!!");
        }
        return jobSave;
    }


    @Override
    @Transactional
    public JobSave updateJob(Job job) {
        JobSave jobSave = new JobSave();

        List<Job> jobs = jobRepository.findJobByTitleAndLevel(job.getTitle(), job.getLevel());
        List<Job> jobs1 = jobs.stream().filter(job1 -> !job1.getStatus().equals(EStatus.CLOSE)).toList();
        Job job2 = !jobs1.isEmpty() ? jobs1.get(0) :null;
        if(job.getId() != null && (jobs.isEmpty() || job2 == null || Objects.equals(job2.getId(), job.getId()))){
            jobRepository.save(job);

            jobSave.setStatus("Success");
            jobSave.setMessage("Update job success!!!");
        } else {
            jobSave.setStatus("Fail");
            jobSave.setMessage("Update job fail!!!");
        }
        return jobSave;

    }


    @Override
    public Job createJobByJobRequest(JobRequest jobRequest) {
        Job job = modelMapper.map(jobRequest, Job.class);

        Level level = levelRepository.findById(jobRequest.getLevel()).orElseThrow();
        job.setLevel(level);

        List<Skill> skills = skillRepository.findAllById(jobRequest.getSkills());
        List<SkillJob> skillJobs = skills.stream().map(skill -> SkillJob.builder().skill(skill).job(job).build()).toList();

        List<Benefits> benefits = benefitsRepository.findAllById(jobRequest.getBenefits());
        List<BenefitsJob> benefitsJobs = benefits.stream().map(benefit -> BenefitsJob.builder().job(job).benefits(benefit).build()).toList();

        job.setSkillJobs(skillJobs);
        job.setBenefitsJobs(benefitsJobs);
        return job;
    }

    @Override
    @Transactional
    public void deleteSkills(List<Long> skillIdOlds, List<Long> skillIds, Long jobId) {
        List<Long> skillIdCommons = new ArrayList<>();
        List<Long> skillIdDeletes = new ArrayList<>(skillIdOlds);
        for (Long i: skillIdOlds) {
            for (Long j: skillIds) {
                if(Objects.equals(i, j)) {
                    skillIdCommons.add(i);
                }
            }
        }
        for (Long i: skillIdCommons) {
            skillIdDeletes.remove(i);
        }

        List<SkillJob> skillJobs = new ArrayList<>();
        for (Long i: skillIdDeletes) {
            SkillJob skillJob = skillJobRepository.findByJob_IdAndSkill_Id(jobId, i);
            skillJobs.add(skillJob);
        }
        skillJobRepository.deleteAll(skillJobs);
    }

    @Override
    @Transactional
    public void deleteBenefits(List<Long> benefitIdOlds, List<Long> benefitIds, Long jobId) {
        List<Long> benefitIdCommons = new ArrayList<>();
        List<Long> benefitIdDeletes = new ArrayList<>(benefitIdOlds);
        for (Long i: benefitIdOlds) {
            for (Long j: benefitIds) {
                if(Objects.equals(i, j)) {
                    benefitIdCommons.add(i);
                }
            }
        }
        for (Long i: benefitIdCommons) {
            benefitIdDeletes.remove(i);
        }

        List<BenefitsJob> benefitsJobs = new ArrayList<>();
        for (Long i: benefitIdDeletes) {
            BenefitsJob benefitsJob = benefitsJobRepository.findByJob_IdAndBenefits_Id(jobId, i);
            benefitsJobs.add(benefitsJob);
        }
        benefitsJobRepository.deleteAll(benefitsJobs);
    }

    @Override
    public List<Job> jobsByImport(List<JobImportFile> jobImportFiles){
        List<Job> jobs = new ArrayList<>();
        for (JobImportFile x: jobImportFiles) {
            List<Long> skillIds = arrSkills(x.getSkills());
            List<Long> benefitsIds = arrBenefits(x.getBenefits());
            Long levelId = getLevelId(x.getLevel());
            x.setSkills(null);
            x.setBenefits(null);
            x.setLevel(null);
            JobRequest jobRequest = modelMapper.map(x, JobRequest.class);
            jobRequest.setSkills(skillIds);
            jobRequest.setBenefits(benefitsIds);
            jobRequest.setLevel(levelId);
            jobRequest.setStatus(EStatus.OPEN);
            Job job = createJobByJobRequest(jobRequest);
            jobs.add(job);
        }
        return jobs;
    }

    @Override
    public List<JobSave> checkValidator(JobRequest jobRequest) {
        List<JobSave> jobSaves = new ArrayList<>();
        if(jobRequest.getSalaryFrom() != null && jobRequest.getSalaryTo() != null &&
                jobRequest.getSalaryFrom().compareTo(jobRequest.getSalaryTo()) > 0){
            jobSaves.add(JobSave.builder().status("false").message( "Salary To must be greater than Salary From").build());
        }

        if(jobRequest.getStartDate() != null && jobRequest.getEndDate() != null &&
                jobRequest.getEndDate().isBefore(jobRequest.getStartDate())){
            jobSaves.add(JobSave.builder().status("false").message(  "End date must be after or equal to start date").build());
        }

        return jobSaves;
    }

    @Override
    public List<Job> findJobByEndDate(LocalDate localDate) {
        return jobRepository.findJobByEndDate(localDate);

    }

    @Override
    @Transactional
    public JobSave saveAllJob(List<Job> jobs) {
        JobSave jobSave = new JobSave();
        for (Job j: jobs) {
            List<Job> jobs2 = jobRepository.findJobByTitleAndLevel(j.getTitle(), j.getLevel());
            List<Job> jobs1 = jobs2.stream().filter(x -> !x.getStatus().equals(EStatus.CLOSE)).toList();
            if (!jobs1.isEmpty()) {
                jobSave.setStatus("Fail");
                jobSave.setMessage("Import jobs by file excel fail!!!");
                return jobSave;
            }
        }
        jobRepository.saveAll(jobs);
        jobSave.setStatus("Success");
        jobSave.setMessage("Import jobs by file excel success!!!");
        return jobSave;
    }

    private List<Long> arrSkills(String string){
        String[] arr = string.split(",");
        String[] newArray = Arrays.stream(arr)
                .map(String::trim)
                .toArray(String[]::new);
        List<String> listSkill = new ArrayList<>(Arrays.asList(newArray));
        List<Skill> skills = skillRepository.findAll();
        List<Long> skillIds = new ArrayList<>();
        for (Skill x: skills) {
            for (String y: listSkill) {
                if(Objects.equals(y, x.getName())) {
                    skillIds.add(x.getId());
                }
            }
        }
        return skillIds;
    }

    private List<Long> arrBenefits(String string){
        String[] arr = string.split(",");
        String[] newArray = Arrays.stream(arr)
                .map(String::trim)
                .toArray(String[]::new);
        List<String> listBenefits = new ArrayList<>(Arrays.asList(newArray));
        List<Benefits> benefits  = benefitsRepository.findAll();
        List<Long> benefitIds = new ArrayList<>();
        for (Benefits x: benefits) {
            for (String y: listBenefits) {
                if(Objects.equals(y, x.getName())) {
                    benefitIds.add(x.getId());
                }
            }
        }
        return benefitIds;
    }

    private Long getLevelId(String levelName) {
        List<Level> levels = levelRepository.findAll();
        Long levelId = null;
        for (Level x: levels) {
            if(Objects.equals(x.getName(), levelName)) {
                levelId = x.getId();
            }
        }
        return levelId;
    }


}
