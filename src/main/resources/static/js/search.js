document.addEventListener("DOMContentLoaded", function() {
    const clearButton = document.getElementById("clearSearch");
    const searchInput = document.getElementById("searchInput");

    clearButton.addEventListener("click", function() {
        searchInput.value = "";
    });
});