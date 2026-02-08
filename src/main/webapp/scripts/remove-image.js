function removeImageCommon(modalSelector) {
    const modal = document.querySelector(modalSelector);
    if (!modal) return;

    const input = modal.querySelector('input[type="file"]');
    const preview = modal.querySelector('.image-preview, .image-preview-edit');
    const img = preview?.querySelector('img');

    if (input) input.value = "";
    if (img) img.src = "";
    if (preview) preview.style.display = "none";
}
