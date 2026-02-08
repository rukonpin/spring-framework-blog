function previewImageCommon(event, imgSelector, previewSelector) {
    const file = event.target.files[0];
    if (!file) return;

    const validTypes = ["image/jpeg", "image/png"];
    if (!validTypes.includes(file.type)) {
        alert("Разрешены только JPG и PNG");
        event.target.value = "";
        return;
    }

    const maxSize = 20 * 1024 * 1024;
    if (file.size > maxSize) {
        alert("Максимальный размер — 20 МБ");
        event.target.value = "";
        return;
    }

    const reader = new FileReader();
    reader.onload = e => {
        document.querySelector(imgSelector).src = e.target.result;
        document.querySelector(previewSelector).style.display = "block";
    };
    reader.readAsDataURL(file);
}
