let cartModalButton = document.getElementById("cart-modal-button");
cartModalButton.addEventListener('click', function () {
    if (document.getElementById("itemNumber").innerText !== "0") {
        $('#shoppingCart').modal('show');
    }
});

setAddRemoveButtonListeners();

function handleAddRemoveEvent(formItems) {
    for (let i = 0; i < formItems.length; i++) {
        let form = formItems[i];
        form.addEventListener('submit', function (event) {
            event.preventDefault();
            event.stopPropagation();
            fetch("/cart", {
                method: "post",
                body: JSON.stringify(getFormFieldsAsObject(form))
            }).then(function (response) {
                if (response.ok) {
                    return response.json();
                } else {
                    response.error();
                }
            }).then(function (data) {
                if (data.items.length > 0) {
                    console.log(data);
                    fillAndAppendCartTemplate(data);
                } else {
                    document.getElementById("itemNumber").innerText = "0";
                    $('#shoppingCart').modal('hide');
                }
            }).catch(function (error) {
                alert(`Error: ${error}\nIf you see this, our testers did a sloppy job, and our developers an even sloppier`)
            });
        });
    }
}

function setAddRemoveButtonListeners() {

    let addItemForms = document.querySelectorAll("form.add");
    let removeItemForms = document.querySelectorAll("form.remove");
    handleAddRemoveEvent(addItemForms);
    handleAddRemoveEvent(removeItemForms);
}

function getFormFieldsAsObject(elements) {
    return [].reduce.call(elements, (data, element) => {

        data[element.name] = element.value;
        return data;

    }, {});
}

function fillAndAppendCartTemplate(data) {
    let cartSource = document.getElementById("cartBody").innerHTML;
    let cartTemplate = Handlebars.compile(cartSource);
    let cartContext = {items: data.items, amount: data.amount};
    let placeToInsertCart = document.getElementById("cart");
    let toAppendCart = cartTemplate(cartContext);
    placeToInsertCart.innerHTML = toAppendCart;
    let itemNumber = data.itemNumber;
    document.getElementById("itemNumber").innerText = itemNumber;
    setAddRemoveButtonListeners();
}