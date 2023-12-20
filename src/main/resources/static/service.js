// Function to update the JSON data in the textarea
function updateJsonDisplay(jsonData) {
    const jsonDisplayElement = document.getElementById("json-display");
    jsonDisplayElement.value = JSON.stringify(jsonData, null, 2); // Beautify JSON for better display
}

class Person {
    constructor(id, firstName, lastName, birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }
}

function create(event) {
    event.preventDefault(); //prevent default submission since asynchronous

    const personIdElement = document.getElementById("person-id");
    const firstNameElement = document.getElementById("first-name");
    const lastNameElement = document.getElementById("last-name");
    const birthDateElement = document.getElementById("birth-date");

    const personIdValue = personIdElement.value;
    const firstNameValue = firstNameElement.value;
    const lastNameValue = lastNameElement.value;
    const birthDateValue = birthDateElement.value;
    const person = new Person(personIdValue, firstNameValue, lastNameValue, birthDateValue);

    const personData = JSON.stringify({
        id: personIdValue,
        firstName: firstNameValue,
        lastName: lastNameValue,
        birthDate: birthDateValue,
    });

    $.ajax({
        type: "POST",
        crossDomain: true,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
        },
        url: "/create",
        data: personData,
        dataType: "JSON",
        success: function (response) {
            updateJsonDisplay(response); // Display the JSON data in the textarea after the request is successful
        },
        error: function (error) {
            updateJsonDisplay("An error occurred during the create request.");
        }
    });
}

async function readAll(event) {
    event.preventDefault(); // Prevent the default form submission

    try {
        const response = await $.ajax({
            type: "GET",
            crossDomain: true,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            url: "/readAll",
        });

        // Display the JSON data in the textarea
        updateJsonDisplay(response);
    } catch (error) {
        updateJsonDisplay("An error occurred during the readAll request.");
    }
}

async function readById(event) {
    event.preventDefault();
    const personIdElement = document.getElementById("person-id");
    const personIdValue = personIdElement.value;

    try {
        const response = await $.ajax({
            type: "GET",
            crossDomain: true,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            url: "/read/" + personIdValue,
        });

        // Display the JSON data in the textarea instead of using stringify popup
        updateJsonDisplay(response);
    } catch (error) {
        updateJsonDisplay("An error occurred during the readById request.");
    }
}

async function update(event) {
    event.preventDefault();

    const personIdElement = document.getElementById("person-id");
    const personIdValue = personIdElement.value;

    const firstNameElement = document.getElementById("first-name");
    const lastNameElement = document.getElementById("last-name");
    const birthDateElement = document.getElementById("birth-date");

    const firstNameValue = firstNameElement.value;
    const lastNameValue = lastNameElement.value;
    const birthDateValue = birthDateElement.value;

    console.log("Updating Person Data:", {
        id: personIdValue,
        firstName: firstNameValue,
        lastName: lastNameValue,
        birthDate: birthDateValue,
    });

    try {
        const response = await $.ajax({
            type: "PUT",
            crossDomain: true,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            url: "/update/" + personIdValue,
            data: JSON.stringify({
                id: personIdValue,
                firstName: firstNameValue,
                lastName: lastNameValue,
                birthDate: birthDateValue,
            }),
        });

        console.log("Update Response:", response);
        // Display the JSON data in the textarea instead of using stringify popup
        updateJsonDisplay(response);
    } catch (error) {
        console.error("Update Error:", error);
        updateJsonDisplay("An error occurred during the update request.");
    }
}

async function deleteThing(event) {
    event.preventDefault();

    const personIdElement = document.getElementById("person-id");
    const personIdValue = personIdElement.value;

    try {
        const response = await $.ajax({
            type: "DELETE",
            crossDomain: true,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            url: "/delete/" + personIdValue,
        });

        // Display the JSON data in the textarea instead of using stringify popup
        updateJsonDisplay(response);
    } catch (error) {
        updateJsonDisplay("An error occurred during the deleteThing request.");
    }
}



