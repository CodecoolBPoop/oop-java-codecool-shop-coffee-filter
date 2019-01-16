let howMany = document.querySelectorAll("span.how-many");

let plusButton = document.querySelectorAll("button.plus-button");
for (let i = 0; i < plusButton.length; i++) {
    plusButton[i].addEventListener('click', function () {
        let many = parseInt(howMany[i].innerText);
        many += 1;
        many = many.toString();
        document.querySelectorAll("span.how-many")[i].innerHTML = many;
        sumPrice();
    });
}

let negativeButton = document.querySelectorAll("button.negative-button");
for (let i = 0; i < negativeButton.length; i++) {
    negativeButton[i].addEventListener('click', function () {
        let many = parseInt(howMany[i].innerText);
        if (many > 0) {
            many -= 1;
            many = many.toString();
            document.querySelectorAll("span.how-many")[i].innerHTML = many;
            sumPrice();
        }
    });
}