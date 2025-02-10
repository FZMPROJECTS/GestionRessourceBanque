document.addEventListener('DOMContentLoaded', function() {
    window.togglePasswordSection = function() {
        const section = document.getElementById('passwordSection');
        const saveButton = document.getElementById('saveButton');
        if (section.style.display === 'none') {
            section.style.display = 'block';
            saveButton.style.display = 'inline-block'; // Affiche le bouton Sauvegarder
        } else {
            section.style.display = 'none';
            saveButton.style.display = 'none'; // Cache le bouton Sauvegarder
        }
    }
});