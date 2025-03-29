

let currentPage = 1;
let pageSize = 10;
let currentImageData = null;
let currentId = null;

$(document).ready(() => {
    populatePageSizeOptions();
    loader.show();
    loadData();
    $('#editModal').on('hidden.bs.modal', resetForm);
});

function populatePageSizeOptions() {
    const options = [5, 10, 20, 50];
    options.forEach(size => {
        $('#pageSize').append(`<option value="${size}" ${size === 10 ? 'selected' : ''}>${size}</option>`);
    });
}

function changePage(newPage, totalPages) {
    if (newPage < 1 || newPage > totalPages) return;
    currentPage = newPage;
    loader.show();
    loadData();
}

function changePageSize() {
    pageSize = parseInt($('#pageSize').val());
    currentPage = 1;
    loader.show();
    loadData();
}

async function loadData() {
    try {
        const { data } = await fetchInvestments(currentPage - 1, pageSize);
        renderTable(data.data, currentPage, pageSize);
        setupPagination(data.totalCount, pageSize, currentPage);
        loader.hide();
    } catch (error) {
        showError('Failed to load data', error);
    }
}

async function openCreate() {
    const {data} = await fetchAllUserLevels();
    console.log(data);
    appendUserLevels(data.data);
    $('#editModal').modal('show');
}

function appendUserLevels(userLevels) {
    $('#availableLevels').empty();
    userLevels.forEach((userLevel, index) => {
        $('#availableLevels').append(
            `
            <div class="form-check form-check-inline" id="availableLevels">
                <input class="form-check-input" type="checkbox" id="${index}" value="${userLevel.id}">
                <label class="form-check-label" for="${index}">${userLevel.name}</label>
            </div>
            `
        )
    });
}

function appendUserLevelsForEdit(userLevels, allUserLevels) {
    $('#availableLevels').empty();
    userLevels.forEach((userLevel, index) => {
        $('#availableLevels').append(
            `
            <div class="form-check form-check-inline" id="availableLevels">
                <input class="form-check-input" type="checkbox" id="${index}" value="${userLevel.id}" checked>
                <label class="form-check-label" for="${index}">${userLevel.name}</label>
            </div>
            `
        )
    });
}

async function openEdit(id) {
    currentId = id;
    $('#modalTitle').text('Edit NFT Investment');
    const {data: allUserLevels} = await fetchAllUserLevels();
    fetchInvestment(id).then(data => {
        $('#name').val(data.data.name);
        appendUserLevelsForEdit(data.data.allowedLevels, allUserLevels)
        new bootstrap.Modal('#editModal').show();
    }).catch(error => showError('Failed to load investment details', error));
}

function resetForm() {
    $('#investmentForm')[0].reset();
    $('#imagePreview').hide();
    currentImageData = null;
}

function saveInvestment() {
    loader.show();
    const payload = {
        id: currentId? currentId: null,
        name: $('#name').val().trim(),
        levels: $('#availableLevels input[type="checkbox"]:checked')
                    .map(function() { return $(this).val(); })
                    .get()
    };

    const method = currentId ? 'PUT' : 'POST';
    createOrUpdateInvestment(method, payload)
        .then(() => {
            toastr.success('Investment saved successfully');
            $('#editModal').modal('hide');
            loadData();
        })
        .catch(error => showError('Failed to save investment', error));
}

function confirmDelete(id) {
    if (!confirm('Are you sure you want to delete this investment?')) return;
    loader.show();
    deleteNFTUploaded(id)
        .then(() => {
            toastr.success('Investment deleted successfully');
            loadData();
        })
        .catch(error => {
            showError('Failed to delete investment', error)
            loader.hide();
        });
}

function showError(message, error) {
    console.error(error);
    toastr.error(message);
}
function renderTable(items, currentPage, pageSize) {
    const tbody = $('#tableBody').empty();
    const mobile = $('#mobileCardBody').empty();
    if (!items?.length) {
        tbody.append(`<tr><td colspan="9" class="text-center py-4 text-muted">No investments found</td></tr>`);
        mobile.append(`<div class="card-item text-center text-muted">No investments found</div>`);
        return;
    }
    items.forEach((item, index) => {
        const itemNumber = (currentPage - 1) * pageSize + index + 1;
        const imageSrc = item.image?.image ? `data:${item.image.contentType};base64,${item.image.image}` : '';
        // Desktop Row
        // Generate allowed levels dynamically using .map() and .join('')
        const allowedLevelsHTML = item.allowedLevels.map(levelItem =>
            `<span class="fw-semibold">${levelItem.name} </span>`
        ).join(',');
        tbody.append(`
            <tr>
                <td class="counter-col">${itemNumber}</td>
                <td class="fw-semibold">${item.name}</td>
                <td class="fw-semibold">${allowedLevelsHTML}</td>
                <td>
                    <button class="btn btn-sm btn-outline-danger" onclick="confirmDelete('${item.id}')"><i class="bi bi-trash"></i></button>
                </td>
            </tr>
        `);
        // Mobile Card
        mobile.append(`
            <div class="card-item">
                <div class="card shadow-sm border-0">
                    <!-- Details Section -->
                    <div class="card-body p-3">
                        <div class="d-flex flex-column gap-2">
                            <!-- Item Number -->
                            <div class="text-muted small">#${itemNumber}</div>

                            <!-- Name -->
                            <div class="fw-semibold">${item.name}</div>
                        </div>
                    </div>

                    <!-- Actions Section -->
                    <div class="card-footer bg-light d-flex justify-content-between align-items-center py-2">
                        <button class="btn btn-sm btn-outline-danger" onclick="confirmDelete('${item.id}')">
                            <i class="bi bi-trash"></i> Delete
                        </button>
                    </div>
                </div>
            </div>
        `);
    });
}



function setupPagination(totalItems, pageSize, currentPage) {
    const pagination = $('#pagination').empty();
    const totalPages = Math.ceil(totalItems / pageSize);
    const maxVisiblePages = 5;
    let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);
    pagination.append(`
        <li class="page-item ${currentPage === 1 ? 'disabled' : ''}">
            <a class="page-link" onclick="changePage(${currentPage - 1}, ${totalPages})">&laquo;</a>
        </li>
    `);

    for (let i = startPage; i <= endPage; i++) {
        pagination.append(`
            <li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" onclick="changePage(${i}, ${totalPages})">${i}</a>
            </li>
        `);
    }

    pagination.append(`
        <li class="page-item ${currentPage === totalPages ? 'disabled' : ''}">
            <a class="page-link" onclick="changePage(${currentPage + 1}, ${totalPages})">&raquo;</a>
        </li>
    `);
}