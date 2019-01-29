// If click cart button on home page
let cartModalButton = document.getElementById("cart-modal-button");
cartModalButton.addEventListener('click', function () {
   sumPrice();
    $('#shoppingCart').modal('show');
});

function sumPrice() {
    let prices = document.querySelectorAll("td.price");
    let quantities = document.querySelectorAll("span.quantity");
    let total = 0;
    for (let i = 0; i < prices.length; i++) {
        let price = parseInt(prices[i].innerText);
        let quantity = parseInt(quantities[i].innerText);
        total = total + (price * quantity);
    }
    total = total.toString();
    document.getElementById("total-price").innerHTML = total;
}
