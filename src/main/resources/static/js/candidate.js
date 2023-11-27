$("#cv").on("change", function () {
    let fileName = this.files[0].name;
    $("#fileName").html(`${fileName} <i class="fa-solid fa-link mt-1 me-2 float-end"></i>`);
});
$(document).ready(function(){
    let multipleCancelButton = new Choices('#skills', {
        removeItemButton: true,
        placeholderValue: "  Select skill"
    });
    $(".choices__inner").append('<i class="fa-solid fa-angle-down mt-2 me-1 float-end" style="font-size: 12px"></i>')
});

