window.onload = function() {
    const uploadImageBtn = document.getElementById("uploadImageBtn");
    uploadImageBtn.addEventListener("click", uploadImage);
};

function uploadImage() {
    const fileInput = document.getElementById("imageUpload");
    const formData = new FormData();
    const loading = document.getElementById("loading");
    const recipeList = document.getElementById("recipeList");

    if (!fileInput.files.length) {
        alert("Please select an image file.");
        return;
    }

    loading.style.display = "block";
    recipeList.innerHTML = "";

    formData.append("file", fileInput.files[0]);

    fetch("http://localhost:8080/api/recipes/v1/recipes/upload-image", {
        method: "POST",
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        loading.style.display = "none";
        displayRecipes(data);
    })
    .catch(error => {
        console.error("Error uploading image:", error);
        loading.style.display = "none";
    });
}

function displayRecipes(data) {
    const recipeList = document.getElementById("recipeList");

    if (data.length === 0) {
        recipeList.innerHTML = "<p>No recipes found. Try different ingredients!</p>";
        return;
    }

    data.forEach(recipe => {
        const li = document.createElement("li");
        li.innerHTML = `
            <h3>${recipe.title}</h3>
            <p><strong>Ingredients:</strong><br> ${recipe.ingredients ? recipe.ingredients.join('<br>') : 'N/A'}</p>
            <p><strong>Instructions:</strong><br> 
               ${recipe.instructions.replace(/\n/g, '<br>')}
            </p>
        `;
        recipeList.appendChild(li);
    });
}
