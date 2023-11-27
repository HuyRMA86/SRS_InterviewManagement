package fa.training.interviewmanagement.service;

import fa.training.interviewmanagement.entities.Job;
import fa.training.interviewmanagement.web.request.JobImportFile;
import fa.training.interviewmanagement.web.request.JobRequest;
import fa.training.interviewmanagement.web.request.JobSave;
import fa.training.interviewmanagement.web.request.JobSearch;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface JobService {

    Page<Job> findAllJob(JobSearch jobSearch);

    Job findJobById(Long id);

    void deleteJobById(Long id);

    JobSave saveJob(Job job);

    JobSave updateJob(Job job);

    Job createJobByJobRequest(JobRequest jobRequest);

    List<Job> findJobByEndDate(LocalDate localDate);

    JobSave saveAllJob(List<Job> jobs);


    void deleteSkills(List<Long> skillIdOlds, List<Long> skillIds, Long jobId);

    void deleteBenefits(List<Long> benefitIdOlds, List<Long> benefitIds, Long jobId);
    List<Job> jobsByImport(List<JobImportFile> jobImportFiles);

    List<JobSave> checkValidator(JobRequest jobRequest);


}
