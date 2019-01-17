// If click cart button on home page
let cartModalButton = document.getElementById("cart-modal-button");
cartModalButton.addEventListener('click', function () {
   sumPrice();
});


function sumPrice() {
    let allPrice = document.querySelectorAll("td.price");
    let howMany = document.querySelectorAll("span.how-many");
    let total = 0;
    for (let i = 0; i < allPrice.length; i++) {
        let price = parseInt(allPrice[i].innerText);
        let many = parseInt(howMany[i].innerText);
        total = total + (price * many);
    }
    total = total.toString();
    document.getElementById("total-price").innerHTML = total;
}
