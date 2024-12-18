// Обработка клика на Upload Files
document.getElementById('uploadFilesInput').addEventListener('change', function () {
    document.getElementById('uploadFilesForm').submit();
});

// Обработка клика на Upload Folders
document.getElementById('uploadFoldersInput').addEventListener('change', function () {
    document.getElementById('uploadFoldersForm').submit();
});

document.addEventListener('DOMContentLoaded', function () {
    const renameModal = document.getElementById('renameModal');

    renameModal.addEventListener('show.bs.modal', function (event) {
        // Получаем кнопку, которая вызвала модальное окно
        const button = event.relatedTarget;

        // Извлекаем данные из data-* атрибутов
        const currentName = button.getAttribute('data-current-name');
        const parentPath = button.getAttribute('data-parent-path');
        const isFolder = button.getAttribute('data-is-folder');

        // Находим элементы в модальном окне и заполняем их
        const modalCurrentNameInput = renameModal.querySelector('#currentFileName');
        const modalParentPathInput = renameModal.querySelector('input[name="parentPath"]');
        const modalIsFolderInput = renameModal.querySelector('input[name="isFolder"]');
        const modalNewNameInput = renameModal.querySelector('input[name="newName"]');

        if (modalCurrentNameInput) modalCurrentNameInput.value = currentName;
        if (modalParentPathInput) modalParentPathInput.value = parentPath;
        if (modalIsFolderInput) modalIsFolderInput.value = isFolder;
        if (modalNewNameInput) modalNewNameInput.value = currentName;
    });

});

