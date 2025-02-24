document.getElementById('getRecipesBtn').addEventListener('click', function() {
    const ingredients = document.getElementById('ingredients').value.split(',').map(item => item.trim());
    fetch(`/api/recipes/v1/recipes/by-ingredients?ingredients=${ingredients.join(',')}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        console.log('API Response:', data); // Debugging
        const recipeList = document.getElementById('recipeList');
        recipeList.innerHTML = '';

        data.forEach(recipe => {
            console.log('Recipe:', recipe); // Debugging each recipe object

            const li = document.createElement('li');
            li.innerHTML = `
                <h3>${recipe.title}</h3>
                <p><strong>Ingredients:</strong><br> ${recipe.ingredients ? recipe.ingredients.join('<br>') : 'N/A'}</p>
                <p><strong>Instructions:</strong><br> 
                   ${recipe.instructions.replace(/\n/g, '<br>')}
                </p>
            `;
            recipeList.appendChild(li);
        });
    })
    .catch(error => console.error('Error:', error));
});

document.getElementById('uploadImageBtn').addEventListener('click', function() {
    const fileInput = document.getElementById('imageUpload');
    const formData = new FormData();
    formData.append('file', fileInput.files[0]);

    fetch('/api/recipes/v1/recipes/upload-image', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        console.log('API Response:', data); // Debugging
        const recipeList = document.getElementById('recipeList');
        recipeList.innerHTML = '';
        data.forEach(recipe => {
            console.log('Recipe:', recipe); // Debugging each recipe object

            const li = document.createElement('li');
            li.innerHTML = `
                <h3>${recipe.title}</h3>
                <p><strong>Ingredients:</strong><br> ${recipe.ingredients ? recipe.ingredients.join('<br>') : 'N/A'}</p>
                <p><strong>Instructions:</strong><br> 
                   ${recipe.instructions.replace(/\n/g, '<br>')}
                </p>
            `;
            recipeList.appendChild(li);
        });
    })
    .catch(error => console.error('Error:', error));
});