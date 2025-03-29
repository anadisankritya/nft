async function fetchInvestments(page, size) {
    const response = await fetch(`/nft/api/investment-types/list?page=${page}&size=${size}`, {
        headers: { 'ngrok-skip-browser-warning': 'true' }
    });
    if (!response.ok) throw new Error(await response.text());
    return response.json();
}

async function fetchAllInvestments() {
    const response = await fetch(`/nft/api/investment-types/list`, {
        headers: { 'ngrok-skip-browser-warning': 'true' }
    });
    if (!response.ok) throw new Error(await response.text());
    const data = response.json();
    return data;
}

async function fetchInvestment(id) {
    const response = await fetch(`/nft/api/investment-types/${id}`, {
        headers: { 'ngrok-skip-browser-warning': 'true' }
    });
    if (!response.ok) throw new Error(await response.text());
    return response.json();
}

async function createOrUpdateInvestment(method, payload) {
    const url = method === 'POST' ? '/nft/api/investment-types/create' : '/nft/api/investment-types/update';
    const response = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json', 'ngrok-skip-browser-warning': 'true' },
        body: JSON.stringify(payload)
    });
    if (!response.ok) throw new Error(await response.text());
    return response.json();
}

async function deleteInvestment(id) {
    const response = await fetch(`/nft/api/investment-types/${id}`, {
        method: 'DELETE',
        headers: { 'ngrok-skip-browser-warning': 'true' }
    });
    if (!response.ok) throw new Error(await response.text());
    return response.json();
}