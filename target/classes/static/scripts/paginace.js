let soucasnaStrana = 1;
let radky = [];

$(function() {
let paginace = document.getElementById("paginace");
 if (paginace) {
    let tabulka = document.getElementById("tabulka");
 radky = document.querySelectorAll("tbody > tr");
 if (radky.length> 5) {
    for(let i = radky.length - 1; i>=0; i--) {
    tabulka.removeChild(radky[i]);
 }
 paginuj(soucasnaStrana);
 }};

function paginuj(soucasnaStrana) {
   $('#paginace').empty();
 for (let i = (soucasnaStrana-1)*5; i <= ((soucasnaStrana-1)*5) + 4; i++) {
   if (radky[i] != null)
   tabulka.appendChild(radky[i]);
}

if(soucasnaStrana>1) {
   $('#paginace').append(`<button id="previousPage" class="page-link">` + (soucasnaStrana - 1) + `</button>`);
  $('#previousPage').click(function() {
      for (let i = (soucasnaStrana-1)*5; i <= ((soucasnaStrana-1)*5) + 4; i++) {
         if (radky[i] != null)
         tabulka.removeChild(radky[i]);}
      soucasnaStrana = soucasnaStrana - 1;
      paginuj(soucasnaStrana);
   });
}

if(radky.length>5) {
   $('#paginace').append(`<span class="page-item active"><button id="currentPage" class="page-link">` + soucasnaStrana + `</button></span>`);
   }

if(radky.length > soucasnaStrana*5) {
   $('#paginace').append(`<button id="nextPage" class="page-link">` + (soucasnaStrana + 1) + `</button>`);
  $('#nextPage').click(function() {
      for (let i = (soucasnaStrana-1)*5; i <= ((soucasnaStrana-1)*5) + 4; i++) {
         if (radky[i] != null)
         tabulka.removeChild(radky[i]);}
      soucasnaStrana = soucasnaStrana + 1;
      paginuj(soucasnaStrana);
   });
}
}
});
