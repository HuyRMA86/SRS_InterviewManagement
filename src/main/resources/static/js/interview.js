$(document).ready(function(){
    new Choices('#interviewId', {
        removeItemButton: true,
        placeholderValue: "  Search interviewer",

    });
    $(".choices__inner").append('<i class="fa-solid fa-angle-down mt-2 me-1 float-end" style="font-size: 12px"></i>')
});