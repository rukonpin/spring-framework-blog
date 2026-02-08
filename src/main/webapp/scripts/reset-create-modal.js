function resetCreateModal() {
    const modal = document.getElementById("createPostModal");
    const form = modal.querySelector("form");

    form.reset();

    const previewContainer = modal.querySelector("#createImagePreview");
    if (previewContainer) {
        previewContainer.style.display = "none";
    }

    const previewImg = modal.querySelector("#createPreviewImg");
    if (previewImg) {
        previewImg.src = "";
    }

    const tagsContainer = modal.querySelector("#selectedTagsContainer");
    if (tagsContainer) {
        tagsContainer.innerHTML = "";
    }

    modal.querySelectorAll(".tag__form").forEach(btn => {
        btn.classList.remove("selected");
    });
}
