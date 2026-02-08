document.addEventListener("DOMContentLoaded", () => {
    const tagButtons = document.querySelectorAll(".tag");

    tagButtons.forEach(button => {
        button.addEventListener("click", () => {
            const currentTag = button.getAttribute("data-tag");
            const url = new URL(window.location.href);

            if (url.searchParams.get("categories") === currentTag) {
                url.searchParams.delete("categories");
            } else {
                url.searchParams.set("categories", currentTag);
                url.searchParams.set("page", "0");

            }

            window.location.href = url.toString();
        });
    });
});
