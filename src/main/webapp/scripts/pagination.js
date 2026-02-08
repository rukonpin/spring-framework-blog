document.addEventListener("DOMContentLoaded", () => {
    const pagination = document.querySelector(".pagination");
    if (!pagination) return;

    let currentPage = Number(pagination.dataset.page);
    const totalPages = Number(pagination.dataset.total);
    const size = Number(pagination.dataset.size);

    const goToPage = (page) => {
        if (page < 0 || page >= totalPages) return;
        window.location.href = `?page=${page}&size=${size}`;
    };

    document.querySelectorAll(".page-list li").forEach(li => {
        li.addEventListener("click", () => {
            goToPage(Number(li.dataset.page));
        });
    });

    document.querySelectorAll(".arrow-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            const action = btn.dataset.page;

            if (action === "prev") {
                goToPage(currentPage - 1);
            }

            if (action === "next") {
                goToPage(currentPage + 1);
            }
        });
    });
});
