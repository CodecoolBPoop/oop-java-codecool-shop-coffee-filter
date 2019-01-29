function setAddRemoveButtonListeners() {

    let quantities = document.querySelectorAll("span.quantity");

    let addItemForms = document.querySelectorAll("form.add");
    for (let i = 0; i < addItemForms.length; i++) {
        let form = addItemForms[i];
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
                if (data.cart == 1) {
                    let items = data.items;
                    console.log(data);
                    fillAndAppendCartTemplate(items);
                } else {
                    $('#shoppingCart').modal('hide');
                }
            }).catch(function (error) {
                alert(`Error: ${error}\nIf you see this, our testers did a sloppy job, and our developers an even sloppier`)
            });
        });
    }

    let removeItemForms = document.querySelectorAll("form.remove");
    for (let i = 0; i < removeItemForms.length; i++) {
        let form = removeItemForms[i];
        form.addEventListener('submit', function () {
            event.preventDefault();
            event.stopPropagation();
            fetch("/cart", {
                method: "post",
                body: JSON.stringify(getFormFieldsAsObject(form))
            }).then(function (response) {
                let quantity = parseInt(quantities[i].innerText);
                quantity--;
                if (quantity < 1) {
                    let toRemove = form.parentElement.parentElement;
                    let removeFrom = toRemove.parentElement;
                    removeFrom.removeChild(toRemove);
                    if (removeFrom.children.length < 2) {
                        $('#shoppingCart').modal('hide');
                    }
                    clearAddRemoveButtonListeners();
                    setAddRemoveButtonListeners();
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
}

function emptyCart() {
    $('#shoppingCart').modal('dispose');
}

setAddRemoveButtonListeners();

function getFormFieldsAsObject(elements) {
    return [].reduce.call(elements, (data, element) => {

        data[element.name] = element.value;
        return data;

    }, {});
}

function fillAndAppendCartTemplate(items) {
    let cartSource = document.getElementById("cart").innerHTML;
    let cartTemplate = Handlebars.compile(cartSource);
    let cartContext = {items: items};
    let placeToInsertCart = document.getElementById("cart");
    let toAppendCart = cartTemplate(cartContext);
    appendToElement(placeToInsertCart, toAppendCart);
}

function appendToElement(elementToExtend, textToAppend) {
    // function to append new DOM elements (represented by a string) to an existing DOM element
    let fakeDiv = document.createElement('div');
    fakeDiv.innerHTML = textToAppend.trim();
    for (let childNode of fakeDiv.childNodes) {
            elementToExtend.appendChild(childNode);
    }
    return elementToExtend.lastChild;
}