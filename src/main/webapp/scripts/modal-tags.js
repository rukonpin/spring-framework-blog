document.addEventListener("DOMContentLoaded", () => {

    document.querySelectorAll(".modal").forEach(modal => {

        const tagButtons = modal.querySelectorAll(".tag__form");
        const container = modal.querySelector("#selectedTagsContainer");

        if (!container) return;

        tagButtons.forEach(button => {
            const tagId = button.dataset.id;

            if (button.classList.contains("selected")) {
                addHiddenTag(container, tagId);
            }

            button.addEventListener("click", () => {
                if (button.classList.contains("selected")) {
                    button.classList.remove("selected");
                    removeHiddenTag(container, tagId);
                } else {
                    button.classList.add("selected");
                    addHiddenTag(container, tagId);
                }
            });
        });
    });

    function addHiddenTag(container, tagId) {
        if (container.querySelector(`input[value="${tagId}"]`)) return;

        const input = document.createElement("input");
        input.type = "hidden";
        input.name = "tagIds";
        input.value = tagId;
        container.appendChild(input);
    }

    function removeHiddenTag(container, tagId) {
        const input = container.querySelector(`input[value="${tagId}"]`);
        if (input) input.remove();
    }
});
