document.addEventListener("DOMContentLoaded", () => {

    const form = document.getElementById("commentForm");
    const textarea = document.getElementById("commentContent");
    const container = document.getElementById("commentsContainer");
    const countEl = document.getElementById("commentsCount");

    if (!form) return;

    const postId = form.dataset.postId;

    form.addEventListener("submit", e => {
        e.preventDefault();

        const content = textarea.value.trim();
        if (!content) return;

        fetch(`/api/comments/${postId}/add`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json; charset=UTF-8"
            },
            body: JSON.stringify({ content })
        })
        .then(res => res.json())
        .then(comment => {

            const html = `
                <div class="comment" data-comment-id="${comment.id}">
                                    <img src="${comment.imagePath}" class="comment__avatar" alt="">
                                    <div>
                                        <div class="comment__header">
                                            <p class="comment__text">${comment.name}</p>
                                            <span class="comment__date">${comment.createdAt}</span>
                                        </div>
                                        <p class="comment__general">${comment.content}</p>
                                        <div class="comment__actions">
                                            <img src="/icons/pencil.svg" class="comment__edit" alt="">
                                            <a class="edit-comment">Редактирование</a>
                                            <img src="/icons/trashcomment.svg" class="comment__trash" alt="">
                                            <a class="delete-comment">Удалить</a>
                                        </div>
                                    </div>
                                </div>
            `;

            container.insertAdjacentHTML("afterbegin", html);
            textarea.value = "";


            countEl.textContent = Number(countEl.textContent) + 1;
        })
        .catch(console.error);
    });
});

document.addEventListener("click", e => {

const countEl = document.getElementById("commentsCount");

    if (e.target.classList.contains("delete-comment")) {
        e.preventDefault();

        const commentEl = e.target.closest(".comment");
        const commentId = commentEl.dataset.commentId;



        fetch(`/api/comments/${commentId}`, { method: "DELETE" })
            .then(res => {
                if (!res.ok) throw new Error();
                commentEl.remove();
                countEl.textContent = Math.max(0, Number(countEl.textContent) - 1);
            })
            .catch(console.error);

    }



    if (e.target.classList.contains("edit-comment")) {
        e.preventDefault();

        const commentEl = e.target.closest(".comment");
        const commentId = commentEl.dataset.commentId;
        const textEl = commentEl.querySelector(".comment__general");
        const btn = e.target;

        if (!commentEl.dataset.editing) {

            commentEl.dataset.editing = "true";
            commentEl.dataset.originalText = textEl.textContent;

            const textarea = document.createElement("textarea");
            textarea.className = "comment__general";
            textarea.value = textEl.textContent;

            textEl.replaceWith(textarea);
            btn.textContent = "Сохранить";
            return;
        }

        const newContent = textEl.value.trim();
        if (!newContent) return;

        fetch(`/api/comments/${commentId}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json; charset=UTF-8"
            },
            body: JSON.stringify({ content: newContent })
        })
        .then(res => {
            if (!res.ok) throw new Error();

            const p = document.createElement("p");
            p.className = "comment__general";
            p.textContent = newContent;

            textEl.replaceWith(p);
            btn.textContent = "Редактирование";

            delete commentEl.dataset.editing;
            delete commentEl.dataset.originalText;
        })
        .catch(console.error);
    }
});
