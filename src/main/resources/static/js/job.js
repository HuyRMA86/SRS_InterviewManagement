
$(document).ready(function(){
new Choices('#skills', {
        removeItemButton: true
    });
    $(".choices__inner").append('<i class="fa-solid fa-angle-down mt-2 me-1 float-end" style="font-size: 12px"></i>')
});

$(document).ready(function(){
    new Choices('#benefits', {
        removeItemButton: true
    });
    $(".choices__inner").append('<i class="fa-solid fa-angle-down mt-2 me-1 float-end" style="font-size: 12px"></i>')
});

