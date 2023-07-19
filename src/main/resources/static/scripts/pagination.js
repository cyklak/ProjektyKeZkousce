let currentPage = 1;
let rows = [];

$(function() {
let pagination = document.getElementById("pagination");
 if (pagination) {
    let table = document.getElementById("table");
 rows = document.querySelectorAll("tbody > tr");
 if (rows.length> 5) {
    for(let i = rows.length - 1; i>=0; i--) {
    table.removeChild(rows[i]);
 }
 paginate(currentPage);
 }};

function paginate(currentPage) {
   $('#pagination').empty();
 for (let i = (currentPage-1)*5; i <= ((currentPage-1)*5) + 4; i++) {
   if (rows[i] != null)
   table.appendChild(rows[i]);
}

if(currentPage>1) {
   $('#pagination').append(`<button id="previousPage" class="page-link">` + (currentPage - 1) + `</button>`);
  $('#previousPage').click(function() {
      for (let i = (currentPage-1)*5; i <= ((currentPage-1)*5) + 4; i++) {
         if (rows[i] != null)
         table.removeChild(rows[i]);}
      currentPage = currentPage - 1;
      paginate(currentPage);
   });
}

if(rows.length>5) {
   $('#pagination').append(`<span class="page-item active"><button id="currentPage" class="page-link">` + currentPage + `</button></span>`);
   }

if(rows.length > currentPage*5) {
   $('#pagination').append(`<button id="nextPage" class="page-link">` + (currentPage + 1) + `</button>`);
  $('#nextPage').click(function() {
      for (let i = (currentPage-1)*5; i <= ((currentPage-1)*5) + 4; i++) {
         if (rows[i] != null)
         table.removeChild(rows[i]);}
      currentPage = currentPage + 1;
      paginate(currentPage);
   });
}
}
});
