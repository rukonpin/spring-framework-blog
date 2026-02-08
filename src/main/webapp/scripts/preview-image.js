function previewCreateImage(event) {
    previewImage(event, "createPreviewImg", "createImagePreview");
}

function previewEditImage(event) {
    previewImage(event, "editPreviewImg", "editImagePreview");
}

function previewImage(event, imgId, previewId) {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = e => {
        document.getElementById(imgId).src = e.target.result;
        document.getElementById(previewId).style.display = "block";
    };
    reader.readAsDataURL(file);
}

function removeCreateImage() {
    removeImage("create-image", "createImagePreview", "createPreviewImg");
}

function removeEditImage() {
    removeImage("edit-image", "editImagePreview", "editPreviewImg");
}

function removeImage(inputId, previewId, imgId) {
    document.getElementById(inputId).value = "";
    document.getElementById(imgId).src = "";
    document.getElementById(previewId).style.display = "none";
}
