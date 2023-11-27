package fa.training.interviewmanagement.config;

import fa.training.interviewmanagement.entities.Candidate;
import fa.training.interviewmanagement.web.request.CandidateRequest;
import org.modelmapper.ExpressionMap;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class CommonBean {

//    final private ExpressionMap<InterviewRequest, InterviewSchedule> MAPPER_INTERVIEW = mapper -> {
//        mapper.when(Conditions.isNotNull()).map(InterviewRequest::getId, InterviewSchedule::setId);
//        mapper.when(Conditions.isNotNull()).map(InterviewRequest::getId, InterviewSchedule::setId);
//        mapper.skip(InterviewSchedule::setInterviewer);
//        mapper.skip(InterviewSchedule::setResultInterviews);
//    };

    private final ExpressionMap<CandidateRequest, Candidate> MAPPER_CANDIDATE = mapper -> {
        mapper.skip(Candidate::setCv);
    };

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.createTypeMap(InterviewRequest.class, InterviewSchedule.class).addMappings(MAPPER_INTERVIEW);
        modelMapper.createTypeMap(CandidateRequest.class, Candidate.class).addMappings(MAPPER_CANDIDATE);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(20);
        executor.setThreadNamePrefix("UserThread -");
        executor.initialize();
        return executor;
    }


}
