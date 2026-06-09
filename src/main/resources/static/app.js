const AUTH_STORAGE_KEY = "cloudstore_basic_auth";
const USER_STORAGE_KEY = "cloudstore_username";

let basicAuth = localStorage.getItem(AUTH_STORAGE_KEY) || "";
let currentUsername = localStorage.getItem(USER_STORAGE_KEY) || "";

function updateAuthStatus() {
    const status = document.getElementById("authStatus");
    if (!status) {
        return;
    }

    status.textContent = currentUsername
        ? `Signed in as ${currentUsername}`
        : "Not signed in";
}

function buildAuthHeader() {
    return basicAuth ? { Authorization: `Basic ${basicAuth}` } : {};
}

function persistAuth(username, password) {
    currentUsername = username;
    basicAuth = btoa(`${username}:${password}`);
    localStorage.setItem(USER_STORAGE_KEY, username);
    localStorage.setItem(AUTH_STORAGE_KEY, basicAuth);
    updateAuthStatus();
}

function clearAuth() {
    currentUsername = "";
    basicAuth = "";
    localStorage.removeItem(USER_STORAGE_KEY);
    localStorage.removeItem(AUTH_STORAGE_KEY);
    updateAuthStatus();
}

async function readResponseBody(response) {
    const contentType = response.headers.get("content-type") || "";
    if (contentType.includes("application/json")) {
        return response.json();
    }
    return response.text();
}

async function registerUser() {
    const username = document.getElementById("registerUsername").value.trim();
    const email = document.getElementById("registerEmail").value.trim();
    const password = document.getElementById("registerPassword").value;

    const response = await fetch("/api/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username,
            email,
            password
        })
    });

    const data = await readResponseBody(response);

    if (!response.ok || data.success === false) {
        alert(data.message || "Registration failed");
        return;
    }

    alert(data.message || "Registered");
}

async function loginUser() {
    const username = document.getElementById("loginUsername").value.trim();
    const password = document.getElementById("loginPassword").value;

    const response = await fetch("/api/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username,
            password
        })
    });

    const data = await readResponseBody(response);

    if (!response.ok || data.success === false) {
        clearAuth();
        alert(data.message || "Login failed");
        return;
    }

    persistAuth(username, password);
    alert(data.message || "Logged in");
}

function renderProduct(product) {
    const card = document.createElement("article");
    card.className = "product-card";

    card.innerHTML = `
        <img src="${product.image}" alt="${product.title}">
        <div class="product-card__body">
            <h3>${product.title}</h3>
            <p class="muted">${product.description}</p>
            <p><strong>Price:</strong> ${product.price}</p>
            <label class="quantity">
                Quantity
                <input id="qty-${product.id}" type="number" min="1" value="1">
            </label>
            <button onclick="createOrder(${product.id})">Order</button>
        </div>
    `;

    return card;
}

async function loadProducts() {
    const response = await fetch("/products");
    const products = await response.json();

    const productDiv = document.getElementById("products");
    productDiv.innerHTML = "";

    products.forEach(product => {
        productDiv.appendChild(renderProduct(product));
    });
}

async function createOrder(productId) {
    if (!currentUsername || !basicAuth) {
        alert("You must log in first");
        return;
    }

    const quantityInput = document.getElementById(`qty-${productId}`);
    const quantity = Number(quantityInput?.value || 1);

    const response = await fetch("/orders", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            ...buildAuthHeader()
        },
        body: JSON.stringify({
            username: currentUsername,
            items: [
                {
                    productId,
                    quantity
                }
            ]
        })
    });

    const data = await readResponseBody(response);

    if (!response.ok) {
        alert(data.error || data.message || "Could not create order");
        return;
    }

    alert("Order created");
    loadOrders();
}

async function loadOrders() {
    if (!currentUsername || !basicAuth) {
        alert("You must log in first");
        return;
    }

    const response = await fetch("/orders", {
        headers: {
            ...buildAuthHeader()
        }
    });

    const orders = await response.json();
    const orderDiv = document.getElementById("orders");
    orderDiv.innerHTML = "";

    const myOrders = orders.filter(order => order.username === currentUsername);

    if (myOrders.length === 0) {
        orderDiv.innerHTML = `<p class="muted">No orders found for ${currentUsername}.</p>`;
        return;
    }

    myOrders.forEach(order => {
        const div = document.createElement("article");
        div.className = "order-card";

        const items = order.items
            .map(item => `${item.productTitle} x ${item.quantity}`)
            .join("<br>");

        div.innerHTML = `
            <p><strong>Order ID:</strong> ${order.id}</p>
            <p><strong>Status:</strong> ${order.status}</p>
            <p><strong>Total:</strong> ${order.total}</p>
            <p>${items}</p>
        `;

        orderDiv.appendChild(div);
    });
}

document.addEventListener("DOMContentLoaded", () => {
    updateAuthStatus();
    loadProducts();
});
