const BASE_URL = 'http://localhost:8080';

async function fetchInvestments(page, size) {
    const response = await fetch(`/nft/api/nft/list?page=${page}&size=${size}`, {
        headers: { 'ngrok-skip-browser-warning': 'true' }
    });
    if (!response.ok) throw new Error(await response.text());
    return response.json();
}

async function fetchInvestment(id) {
    const response = await fetch(`/nft/api/nft/${id}`, {
        headers: { 'ngrok-skip-browser-warning': 'true' }
    });
    if (!response.ok) throw new Error(await response.text());
    return response.json();
}

async function createOrUpdateInvestment(method, payload) {
    const url = method === 'POST' ? '/nft/api/nft/create' : '/nft/api/nft/update';
    const response = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json', 'ngrok-skip-browser-warning': 'true' },
        body: JSON.stringify(payload)
    });
    if (!response.ok) throw new Error(await response.text());
    return response.json();
}

async function deleteInvestment(id) {
    const response = await fetch(`/nft/api/nft/${id}`, {
        method: 'DELETE',
        headers: { 'ngrok-skip-browser-warning': 'true' }
    });
    if (!response.ok) throw new Error(await response.text());
    return response.json();
}