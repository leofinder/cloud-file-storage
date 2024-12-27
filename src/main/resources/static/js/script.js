document.getElementById('uploadFilesInput').addEventListener('change', function () {
    const input = this;
    if (validateFileSizes(input)) {
        showLoadingModal();
        document.getElementById('uploadFilesForm').submit();
    } else {
        input.value = '';
    }
});

document.getElementById('uploadFoldersInput').addEventListener('change', function () {
    const input = this;
    if (validateFileSizes(input)) {
        showLoadingModal();
        document.getElementById('uploadFoldersForm').submit();
    } else {
        input.value = '';
    }
});

function validateFileSizes(input) {
    const MAX_FILE_SIZE_MB = 10;
    const MAX_TOTAL_SIZE_MB = 100;

    const files = input.files;
    const maxFileSizeBytes = MAX_FILE_SIZE_MB * 1024 * 1024;
    const maxTotalSizeBytes = MAX_TOTAL_SIZE_MB * 1024 * 1024;

    let totalSize = 0;

    const fileSizeErrorMessages =
        `An error occurred while uploading files.
        The maximum allowed size for a single file is ${MAX_FILE_SIZE_MB} MB.
        The maximum total size for all files is ${MAX_TOTAL_SIZE_MB} MB.
    `;

    for (const file of files) {
        if (file.size > maxFileSizeBytes) {
            showError(fileSizeErrorMessages);
            return false;
        }
        totalSize += file.size;

        if (totalSize > maxTotalSizeBytes) {
            showError(fileSizeErrorMessages);
            return false;
        }
    }

    return true;
}

function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'alert alert-danger alert-dismissible fade show slide-in"';
    errorDiv.setAttribute('role', 'alert');
    errorDiv.style = 'position: fixed; bottom: 20px; right: 20px; z-index: 1050;';

    errorDiv.innerHTML = `
        <div class="d-flex gap-2 align-items-start">
            <span><i class="fa-solid fa-circle-exclamation icon-danger"></i></span>
            <div class="d-flex flex-column gap-1">
                <h6 class="mb-0">Operation Failed</h6>
                <p class="mb-0 preserve-whitespace">${message}</p>
            </div>
            <button type="button" class="btn-close ms-3" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;

    document.body.appendChild(errorDiv);

    setTimeout(() => {
        errorDiv.classList.remove('show');
        errorDiv.addEventListener('transitionend', () => {
            errorDiv.remove();
        }, { once: true });
    }, 5000);
}

function showLoadingModal() {
    const loadingModal = new bootstrap.Modal(document.getElementById('loadingModal'));
    loadingModal.show();
}

document.addEventListener('DOMContentLoaded', function () {
    const renameModal = document.getElementById('renameModal');

    renameModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;

        const currentName = button.getAttribute('data-current-name');
        const parentPath = button.getAttribute('data-parent-path');
        const isFolder = button.getAttribute('data-is-folder');

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

document.addEventListener('DOMContentLoaded', function () {
    const deleteModal = document.getElementById('deleteModal');

    deleteModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;

        const name = button.getAttribute('data-name');
        const parentPath = button.getAttribute('data-parent-path');
        const isFolder = button.getAttribute('data-is-folder');

        const modalNameInput = deleteModal.querySelector('input[name="name"]');
        const modalParentPathInput = deleteModal.querySelector('input[name="parentPath"]');
        const modalIsFolderInput = deleteModal.querySelector('input[name="isFolder"]');
        const modalText = deleteModal.querySelector('strong');

        if (modalNameInput) modalNameInput.value = name;
        if (modalParentPathInput) modalParentPathInput.value = parentPath;
        if (modalIsFolderInput) modalIsFolderInput.value = isFolder;
        if (modalText) modalText.textContent = name || "this item";
    });
});

setTimeout(() => {
    const alert = document.querySelector('.alert');
    if (alert) {
        alert.classList.remove('show');
        alert.addEventListener('transitionend', () => alert.remove());
    }
}, 5000);

function disableSubmitButton(form) {
    const submitButton = form.querySelector('button[type="submit"]');
    submitButton.disabled = true;
    submitButton.textContent = 'Processing...';
}