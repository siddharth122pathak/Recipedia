window.onload = function() {
    fetchrecent();
};

function fetchrecent() {
    const userId = sessionStorage.getItem('userId');

    fetch(`http://localhost:8080/api/recipes/v1/recipes/recent`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('API Data:', data);
            displayrecent(data); // Corrected the name to match the actual function
        })
        .catch(error => console.error('Error fetching recent recipes:', error));
}

function displayrecent(data) {
    const recipesList = document.getElementById('recentList');

    if (data.length === 0) {
        recipesList.innerHTML = '<p>No recent recipes found.</p>';
        return;
    }

    data.forEach(recipeHistory => {
        const historyItem = document.createElement('div');
        historyItem.className = 'history-item';

        // Ensure you use recipeHistory.user_recipe to access the recipes
        let recipesContent = recipeHistory.user_recipe.map(recipe => {
            return `<h3>${recipe.title}</h3>
                    <p><strong>Ingredients:</strong>${recipe.ingredients.join(', ')}</p>
                    <p><strong>Instructions:</strong>${recipe.instructions.replace(/\n/g, '<br>')}</p>`;
        }).join('');

        historyItem.innerHTML = `
            <h2>Query Date: ${new Date(recipeHistory.created_at).toLocaleDateString()}</h2>
            <p><strong>Ask:</strong> ${recipeHistory.user_ask.join(', ')}</p>
            ${recipesContent}
            <hr>`;
        recipesList.appendChild(historyItem);
    });
}