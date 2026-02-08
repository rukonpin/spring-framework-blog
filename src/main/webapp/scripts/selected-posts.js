document.addEventListener("DOMContentLoaded", () => {
    const select = document.getElementById("pageSizeSelect");
    const selectedValue = select.querySelector(".selected-value");
    const optionsList = select.querySelector(".options-list");

    select.addEventListener("click", () => {
        optionsList.style.display =
            optionsList.style.display === "block" ? "none" : "block";
    });

    optionsList.querySelectorAll("li").forEach(option => {
        option.addEventListener("click", () => {
            const size = option.dataset.value;
            const url = new URL(window.location.href);

            url.searchParams.set("size", size);
            url.searchParams.set("page", 0);

            window.location.href = url.toString();
        });
    });
});
