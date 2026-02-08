document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll("[data-open]").forEach(btn => {
        btn.addEventListener("click", () => {
            const modalId = btn.dataset.open === "create-modal"
                ? "createPostModal"
                : "editPostModal";

            document.getElementById(modalId)?.classList.add("open");
        });
    });

    document.querySelectorAll(".modal").forEach(modal => {
        modal.querySelector(".modal__overlay")?.addEventListener("click", () => {
            modal.classList.remove("open");
            resetCreateModal();
        });

        modal.querySelector("#closeModal")?.addEventListener("click", () => {
            modal.classList.remove("open");
            resetCreateModal();
        });
    });
});
