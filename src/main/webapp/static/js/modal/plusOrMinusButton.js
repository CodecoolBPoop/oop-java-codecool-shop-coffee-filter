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

function clearAddRemoveButtonListeners() {
    let addItemForms = document.querySelectorAll("form.add");
    for (let i = 0; i < addItemForms.length; i++) {
        let form = addItemForms[i];
        form.removeEventListener('submit', setAddRemoveButtonListeners);
    }
    let removeItemForms = document.querySelectorAll("form.remove");
    for (let i = 0; i < removeItemForms.length; i++) {
        let form = removeItemForms[i];
        form.removeEventListener('submit', setAddRemoveButtonListeners);
    }
}

setAddRemoveButtonListeners();

function getFormFieldsAsObject(elements) {
    return [].reduce.call(elements, (data, element) => {

        data[element.name] = element.value;
        return data;

    }, {});
}
