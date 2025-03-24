let currentPage = 1;
let pageSize = 10;
let currentImageData = null;
let currentId = null;

const loader = document.getElementById('loader');

function enableLoader() {
    console.log("enable loder start");
    loader.classList.add('active');
}

function disableLoader() {
    loader.classList.remove('active');
    console.log("enable loader end");
}

$(document).ready(() => {
    populatePageSizeOptions();
    enableLoader();
    loadData();
    $('#editModal').on('hidden.bs.modal', resetForm);
    $('#image').on('change', handleImageUpload);
});

function populatePageSizeOptions() {
    const options = [5, 10, 20, 50];
    options.forEach(size => {
        $('#pageSize').append(`<option value="${size}" ${size === 10 ? 'selected' : ''}>${size}</option>`);
    });
}

async function loadData() {
    try {
        const { data, totalElements } = await fetchInvestments(currentPage - 1, pageSize);
        renderTable(data, currentPage, pageSize);
        setupPagination(totalElements, pageSize, currentPage, changePage);
        disableLoader();
    } catch (error) {
        showError('Failed to load data', error);
    }
}

function changePage(newPage) {
    if (newPage < 1 || newPage > totalPages) return;
    currentPage = newPage;
    enableLoader();
    loadData();
}

function changePageSize() {
    pageSize = parseInt($('#pageSize').val());
    currentPage = 1;
    enableLoader();
    loadData();
}

function openCreate() {
    currentId = null;
    $('#modalTitle').text('Create NFT Investment');
    resetForm();
}

function openEdit(id) {
    currentId = id;
    $('#modalTitle').text('Edit NFT Investment');
    fetchInvestment(id).then(data => {
        $('#name').val(data.data.name);
        $('#ownerName').val(data.data.ownerName);
        $('#buyPrice').val(data.data.buyPrice);
        $('#profit').val(data.data.profit);
        $('#investmentType').val(data.data.investmentType);
        $('#allowedLevel').val(data.data.allowedLevel);
        $('#status').val(`${data.data.status}`);
        if (data.data.image?.image) {
            $('#imagePreviewContainer').attr('src', `data:${data.data.image.contentType};base64,${data.data.image.image}`).show();
        } else {
            $('#imagePreviewContainer').hide();
        }
        new bootstrap.Modal('#editModal').show();
    }).catch(error => showError('Failed to load investment details', error));
}

function handleImageUpload(event) {
    const file = event.target.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = e => {
        currentImageData = e.target.result.split(',')[1];
        $('#imagePreview').attr('src', e.target.result).show();
    };
    reader.readAsDataURL(file);
}

function resetForm() {
    $('#investmentForm')[0].reset();
    $('#imagePreview').hide();
    currentImageData = null;
}

function saveInvestment() {
    enableLoader();
    const payload = {
        name: $('#name').val(),
        ownerName: $('#ownerName').val(),
        buyPrice: parseFloat($('#buyPrice').val()),
        profit: parseFloat($('#profit').val()),
        investmentType: $('#investmentType').val(),
        status: $('#status').val() === 'true',
        image: currentImageData ? { contentType: $('#image')[0].files[0].type, image: currentImageData } : null
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
    enableLoader();
    deleteInvestment(id)
        .then(() => {
            toastr.success('Investment deleted successfully');
            loadData();
        })
        .catch(error => {
            showError('Failed to delete investment', error)
            disableLoader();
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
        tbody.append(`
            <tr>
                <td class="counter-col">${itemNumber}</td>
                <td class="fw-semibold">${item.name}</td>
                <td>${item.ownerName}</td>
                <td>${imageSrc ? `<img src="${imageSrc}" class="image-preview">` : 'N/A'}</td>
                <td>$${(item.buyPrice || 0).toFixed(2)}</td>
                <td class="${item.profit >= 0 ? 'text-success' : 'text-danger'}">$${(item.profit || 0).toFixed(2)}</td>
                <td>${item.investmentType}</td>
                <td><span class="badge bg-${item.status ? 'success' : 'danger'}">${item.status ? 'Active' : 'Inactive'}</span></td>
                <td>
                    <button class="btn btn-sm btn-outline-primary me-2" onclick="openEdit('${item.id}')"><i class="bi bi-pencil"></i></button>
                    <button class="btn btn-sm btn-outline-danger" onclick="confirmDelete('${item.id}')"><i class="bi bi-trash"></i></button>
                </td>
            </tr>
        `);
        // Mobile Card
        mobile.append(`
            <div class="card-item">
                <div class="card shadow-sm border-0">
                    <!-- Image Section -->
                    <div class="card-img-top p-3">
                        ${item.image?.image
                            ? `<img src="data:${item.image.contentType};base64,${item.image.image}" alt="${item.name}" class="img-fluid rounded">`
                            : `<div class="text-muted small">No Image</div>`}
                    </div>

                    <!-- Details Section -->
                    <div class="card-body p-3">
                        <div class="d-flex flex-column gap-2">
                            <!-- Name -->
                            <div class="fw-semibold">${item.name}</div>

                            <!-- Item Number -->
                            <div class="text-muted small">#${itemNumber}</div>

                            <!-- Owner Name -->
                            <div class="text-muted small">Owner: ${item.ownerName}</div>

                            <!-- Buy Price -->
                            <div class="text-success fw-semibold">Buy Price: $${(item.buyPrice || 0).toFixed(2)}</div>

                            <!-- Profit -->
                            <div class="${item.profit >= 0 ? 'text-success' : 'text-danger'} fw-semibold">
                                Profit: $${(item.profit || 0).toFixed(2)}
                            </div>

                            <!-- Investment Type -->
                            <div class="text-muted small">Type: ${item.investmentType}</div>

                            <!-- Allowed Levels -->
                            <div class="text-muted small">Levels: ${item.allowedLevel}</div>

                            <!-- Status -->
                            <div>
                                <span class="badge bg-${item.status ? 'success' : 'danger'} small">
                                    ${item.status ? 'Active' : 'Inactive'}
                                </span>
                            </div>
                        </div>
                    </div>

                    <!-- Actions Section -->
                    <div class="card-footer bg-light d-flex justify-content-between align-items-center py-2">
                        <button class="btn btn-sm btn-outline-primary" onclick="openEdit('${item.id}')">
                            <i class="bi bi-pencil"></i> Edit
                        </button>
                        <button class="btn btn-sm btn-outline-danger" onclick="confirmDelete('${item.id}')">
                            <i class="bi bi-trash"></i> Delete
                        </button>
                    </div>
                </div>
            </div>
        `);
    });
}

function setupPagination(totalItems, pageSize, currentPage, onPageChange) {
    const pagination = $('#pagination').empty();
    const totalPages = Math.ceil(totalItems / pageSize);
    const maxVisiblePages = 5;
    let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);

    pagination.append(`
        <li class="page-item ${currentPage === 1 ? 'disabled' : ''}">
            <a class="page-link" onclick="onPageChange(${currentPage - 1})">&laquo;</a>
        </li>
    `);

    for (let i = startPage; i <= endPage; i++) {
        pagination.append(`
            <li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" onclick="onPageChange(${i})">${i}</a>
            </li>
        `);
    }

    pagination.append(`
        <li class="page-item ${currentPage === totalPages ? 'disabled' : ''}">
            <a class="page-link" onclick="onPageChange(${currentPage + 1})">&raquo;</a>
        </li>
    `);
}