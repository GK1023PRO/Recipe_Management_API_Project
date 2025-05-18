document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('recipeForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const notificationEl = document.getElementById('notification');

        const recipe = {
            title: document.getElementById('title').value,
            ingredients: document.getElementById('ingredients').value.split(',').map(item => item.trim()),
            instructions: document.getElementById('instructions').value,
            cookingTime: parseInt(document.getElementById('cookingTime').value),
            category: document.getElementById('category').value
        };

        try {
            const response = await fetch('/api/recipes', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(recipe)
            });

            if (response.ok) {
                notificationEl.textContent = 'Recipe created successfully!';
                notificationEl.className = 'message success';
                notificationEl.style.display = 'block';

                // Reset form
                document.getElementById('recipeForm').reset();

                // Hide notification after 3 seconds
                setTimeout(() => {
                    notificationEl.style.display = 'none';
                }, 3000);
            } else {
                const errorText = await response.text();
                notificationEl.textContent = 'Error creating recipe: ' + errorText;
                notificationEl.className = 'message error';
                notificationEl.style.display = 'block';
            }
        } catch (error) {
            console.error('Error:', error);
            notificationEl.textContent = 'Error connecting to server';
            notificationEl.className = 'message error';
            notificationEl.style.display = 'block';
        }
    });
});
