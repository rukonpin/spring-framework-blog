document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".reaction-like").forEach(like => {
        like.addEventListener("click", () => {
            const postId = like.dataset.postId;
            const countEl = like.querySelector(".like-count");
            let count = parseInt(countEl.textContent, 10);

            if (like.classList.contains("liked")) {
                fetch(`/api/posts/${postId}/likes`, {
                    method: 'DELETE'
                }).then(response => {
                    if (response.ok) {
                        like.classList.remove("liked");
                        countEl.textContent = count - 1;
                    } else {
                        console.error("Ошибка при снятии лайка");
                    }
                }).catch(err => console.error(err));
            } else {
                fetch(`/api/posts/${postId}/likes`, {
                    method: 'POST'
                }).then(response => {
                    if (response.ok) {
                        like.classList.add("liked");
                        countEl.textContent = count + 1;
                    } else {
                        console.error("Ошибка при лайке");
                    }
                }).catch(err => console.error(err));
            }
        });
    });
});
