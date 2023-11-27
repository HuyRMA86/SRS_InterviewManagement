package fa.training.interviewmanagement.controller;

import fa.training.interviewmanagement.entities.*;
import fa.training.interviewmanagement.enums.EStatus;
import fa.training.interviewmanagement.repository.CandidateRepository;
import fa.training.interviewmanagement.repository.DepartmentRepository;
import fa.training.interviewmanagement.repository.LevelRepository;
import fa.training.interviewmanagement.service.*;
import fa.training.interviewmanagement.utils.SessionUtil;
import fa.training.interviewmanagement.web.request.OfferRequest;
import fa.training.interviewmanagement.web.request.OfferSearch;
import fa.training.interviewmanagement.web.resp.CandidateResp;
import fa.training.interviewmanagement.web.resp.OfferExport;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/offer")
@RequiredArgsConstructor
public class OfferController {
    final private CandidateRepository candidateRepository;
    final private CandidateService candidateService;
    final private UserService userService;
    final private LevelRepository levelRepository;
    final private DepartmentRepository departmentRepository;
    final private ResultInterviewService resultInterviewService;
    final private OfferService offerService;
    final private FileService<OfferExport> fileService;
    final private ModelMapper modelMapper;
    private final SessionUtil sessionUtil;

    @GetMapping
    public String getOffer(@ModelAttribute OfferSearch offerSearchReq,
                           @RequestParam(required = false) String messageExport,
                           HttpServletRequest req,
                           Model model) {
        sessionUtil.setTitle(req,"OFFER");
        OfferSearch offerSearch = checkInputSearch(offerSearchReq);
        Page<Offer> offers = offerService.findAllOffer(offerSearch);
        List<Department> departments = departmentRepository.findAll();
        int pageMax = offers.getTotalPages();
        if (pageMax > 0) {
            List<Integer> integers = new ArrayList<>();
            for (int i = 0; i < pageMax; i++) {
                integers.add(i + 1);
            }
            offerSearchReq.setPageMaxNumber(integers);
        } else {
            offerSearchReq.setMessage("Offer don't found");
        }
        model.addAttribute("departments", departments);
        model.addAttribute("offerSearch", offerSearch);
        model.addAttribute("offers", offers);
        model.addAttribute("messageExport", messageExport);
        return "ui/offer/offer-list";
    }

    @PostMapping
    public String postOffer(RedirectAttributes attributes) {
        attributes.addAttribute("messageExport", "Don't found offers");
        return "redirect:/offer";
    }


    @GetMapping("/{id}")
    public String detailOffer(Model model, @ModelAttribute OfferRequest offerRequest, @PathVariable Long id){
        Offer offer = offerService.findOfferById(id).orElseThrow();
        ResultInterview resultInterview = resultInterviewService
                .findResultInterviewByCandidate_Id(offer.getResultInterview().getCandidate().getId())
                .orElseThrow();
        List<InterviewerSchedule> interviewerSchedules = resultInterview.getInterviewSchedule().getInterviewer();
        model.addAttribute("interviewer", interviewerSchedules);
        model.addAttribute("offer", offer);
        return "ui/offer/offer-detail";
    }

    @GetMapping("/approve/{id}")
    public String getApproveOffer(@PathVariable Long id, Model model){
        Offer offer = offerService.findOfferById(id).orElseThrow();
        ResultInterview resultInterview = resultInterviewService
                .findResultInterviewByCandidate_Id(offer.getResultInterview().getCandidate().getId())
                .orElseThrow();
        List<InterviewerSchedule> interviewerSchedules = resultInterview.getInterviewSchedule().getInterviewer();
        model.addAttribute("interviewer", interviewerSchedules);
        model.addAttribute("offer", offer);
        return "ui/offer/offer-approve";
    }

    @GetMapping("/approve/accepted/{id}")
    @ResponseBody
    public void approveOffer(@PathVariable Long id ){
        Offer offer = offerService.findOfferById(id).orElseThrow();
        offer.setStatus(EStatus.APPROVED_OFFER);
        offerService.saveOffer(offer);
        Long candidateId = offer.getResultInterview().getCandidate().getId();
        Candidate candidate = candidateService.findCandidateById(candidateId).orElseThrow();
        candidate.setStatus(EStatus.ACCEPTED_OFFER);
        candidateService.saveCandidate(candidate);
    }

    @GetMapping("/approve/rejected/{id}")
    @ResponseBody
    public void rejectOffer(@PathVariable Long id ){
        Offer offer = offerService.findOfferById(id).orElseThrow();
        offer.setStatus(EStatus.REJECTED_OFFER);
        offerService.saveOffer(offer);
        Long candidateId = offer.getResultInterview().getCandidate().getId();
        Candidate candidate = candidateService.findCandidateById(candidateId).orElseThrow();
        candidate.setStatus(EStatus.DECLINED_OFFER);
        candidateService.saveCandidate(candidate);
    }


    @GetMapping("/create")
    public String getCreateOffer(Model model) {
        List<Candidate> candidates = candidateService.findAll();
        List<Users> users = userService.getUserByRoleRecruiterAndManager();
        List<Level> levels = levelRepository.findAll();
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("departments", departments);
        model.addAttribute("levels", levels);
        model.addAttribute("candidates", candidates);
        return "ui/offer/offer-add";
    }

    @PostMapping("/create")
    public String postCreateOffer(@ModelAttribute OfferRequest offerRequest, Model model, RedirectAttributes re) {
        Offer offer = modelMapper.map(offerRequest, Offer.class);
        ResultInterview resultInterview = resultInterviewService.findResultInterviewByCandidate_Id(offerRequest.getCandidateId()).orElseThrow();
        Users recruiter = userService.findUserByID(offerRequest.getRecruiterId());
        Users manager = userService.findUserByID(offerRequest.getApprovedBy());
        Department department = departmentRepository.findById(offerRequest.getDepartment()).orElseThrow();
        Level level = levelRepository.findById(offerRequest.getLevel()).orElseThrow();
        offer.setLevel(level);
        offer.setDepartment(department);
        offer.setStatus(EStatus.WAITING_FOR_APPROVAL);
        offer.setRecruiter(recruiter);
        offer.setManager(manager);
        offer.setResultInterview(resultInterview);
        offerService.saveOffer(offer);

        re.addAttribute("offer", offer);
        re.addFlashAttribute("message", "Create Success");
        return "redirect:/offer/create";
    }

    @GetMapping("/edit/{id}")
    public String getOfferEdit(@PathVariable Long id, Model model){
        Offer offer = offerService.findOfferById(id).orElseThrow();
        List<Users> users = userService.getUserByRoleRecruiterAndManager();
        List<Level> levels = levelRepository.findAll();
        List<Department> departments = departmentRepository.findAll();
        List<InterviewerSchedule> interviewerSchedules = offer.getResultInterview().getInterviewSchedule().getInterviewer();
        model.addAttribute("users", users);
        model.addAttribute("departments", departments);
        model.addAttribute("levels", levels);
        model.addAttribute("offer", offer);
        model.addAttribute("interviewer", interviewerSchedules);
        return "ui/offer/offer-edit";
    }

    @PostMapping("/edit/{id}")
    public String offerEdit(@PathVariable Long id,
                            RedirectAttributes ra,
                            OfferRequest offerRequest){
        Offer offer = offerService.findOfferById(id).orElseThrow();
        ResultInterview resultInterview = resultInterviewService
                .findResultInterviewByCandidate_Id(offer.getResultInterview().getCandidate().getId()).orElseThrow();
        modelMapper.map(offerRequest,offer);
        Users recruiter = userService.findUserByID(offerRequest.getRecruiterId());
        Users manager = userService.findUserByID(offerRequest.getApprovedBy());
        Department department = departmentRepository.findById(offerRequest.getDepartment()).orElseThrow();
        Level level = levelRepository.findById(offerRequest.getLevel()).orElseThrow();
        offer.setLevel(level);
        offer.setDepartment(department);
        offer.setRecruiter(recruiter);
        offer.setManager(manager);
        offer.setResultInterview(resultInterview);
        offerService.saveOffer(offer);
        ra.addFlashAttribute("message","Ok");
        return "redirect:/offer/edit/" +id;
    }

    @GetMapping("/getResultCandidate/{id}")
    @ResponseBody
    public CandidateResp getCandidateResp(@PathVariable Long id) {
        ResultInterview resultInterview = resultInterviewService.findResultInterviewByCandidate_Id(id)
                .orElseThrow();
        List<InterviewerSchedule> interviewerSchedules = resultInterview.getInterviewSchedule().getInterviewer();
        List<String> interview = interviewerSchedules.stream().map(x -> x.getInterviewer().getFullName()).toList();
        return CandidateResp.builder()
                .eStatus(resultInterview.getCandidate().getStatus())
                .notes(resultInterview.getNote())
                .interviews(interview)
                .build();
    }

    @GetMapping("/delete/{id}")
    public String deleteInterview(@PathVariable Long id) {
        offerService.deleteOffer(id);
        System.out.println("22");
        return "redirect:/offer";
    }

    @PostMapping("/export")
    public void exportOffer(@RequestParam LocalDate fromDate,
                            @RequestParam LocalDate toDate,
                            HttpServletRequest req,
                            HttpServletResponse resp) throws IOException, ServletException {
        List<Offer> offers = offerService.findAllOfferByDate(fromDate, toDate);
        List<OfferExport> exports = offers.stream().map(this::getOfferExport).toList();
        if (!exports.isEmpty()) {
            fileService.export(resp, exports);
        } else {
            req.getRequestDispatcher("/offer").forward(req, resp);
        }
    }

    private OfferSearch checkInputSearch(OfferSearch offerSearch) {
        if (offerSearch.getPageNumber() == null) {
            offerSearch.setPageNumber(1);
        }
        if (offerSearch.getParam() == null || offerSearch.getParam().isEmpty()) {
            offerSearch.setParam("");
        }
        if (offerSearch.getDepartment() == null || offerSearch.getDepartment().isEmpty()) {
            offerSearch.setDepartment("");
        }
        return offerSearch;
    }

    private OfferExport getOfferExport(Offer offer) {
        OfferExport offerExport = new OfferExport();
        offerExport.setApproved(offer.getManager().getFullName());
        offerExport.setCandidateName(offer.getResultInterview().getCandidate().getFullName());
        offerExport.setEmail(offer.getResultInterview().getCandidate().getEmail());
        offerExport.setInterviewNotes(offer.getResultInterview().getNote());
        offerExport.setDepartment(offer.getDepartment().getName());
        offerExport.setStatus(offer.getStatus().name());
        offerExport.setContractType(offer.getContractType().name());
        offerExport.setCreateDate(offer.getCreateDate().toString());
        offerExport.setUpdateDate(offer.getUpdateDate().toString());
        offerExport.setLevel(offer.getLevel().getName());
        offerExport.setDueDate(offer.getDueDate().toString());
        offerExport.setBasicSalary(offer.getBasicSalary().toString());
        offerExport.setNotes(offer.getNotes());
        return offerExport;
    }

}

