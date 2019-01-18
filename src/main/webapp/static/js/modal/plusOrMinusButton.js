let quantities = document.querySelectorAll("span.quantity");

let addItemForms = document.querySelectorAll("form.add");
for (let i = 0; i < addItemForms.length; i++) {
    let form = addItemForms[i];
    form.addEventListener('submit', function (event) {
        event.preventDefault();
        event.stopPropagation();
        let formData = new FormData(event.currentTarget);
        fetch("/cart", {
            method: "post",
            body: formData
        }).then(function (response) {
            let quantity = parseInt(quantities[i].innerText);
            quantity += 1;
            quantity = quantity.toString();
            document.querySelectorAll("span.quantity")[i].innerHTML = quantity;
            sumPrice();
        }).catch(function (error) {
            alert(`Error: ${error}
            If you see this, our testers did a sloppy job, and our developers an even sloppier`)
        });
    });
}

let removeItemForms = document.querySelectorAll("form.remove");
for (let i = 0; i < removeItemForms.length; i++) {
    let form = removeItemForms[i];
    form.addEventListener('submit', function () {
        event.preventDefault();
        event.stopPropagation();
        let formData = new FormData(event.currentTarget);
        fetch("/cart", {
            method: "post",
            body: new FormData
        }).then(function (response) {
            let quantity = parseInt(quantities[i].innerText);
            quantity--;
            if (quantity < 1) {
                let toRemove = form.parentNode.parentNode;
                let removeFrom = toRemove.parentNode;
                if (removeFrom.childNodes.length < 2) {
                    emptyCart();
                }
                removeFrom.removeChild(toRemove);
                
            } else {
                quantity = quantity.toString();
                document.querySelectorAll("span.quantity")[i].innerHTML = quantity;
                sumPrice();
            }
        }).catch(function (error) {
            alert(`Error: ${error}
            If you see this, our testers did a sloppy job, and our developers an even sloppier`)
        });
    });
}

function emptyCart() {
    $('#shoppingCart').modal('hide');
}