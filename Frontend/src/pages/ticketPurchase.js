import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ConcertClient from "../api/concertClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class TicketPurchase extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onStateChange', 'onSelectConcert', 'onReserveTicket', 'onPurchaseTicket'], this);
        this.dataStore = new DataStore();

        // Possible States of the page
        this.LOADING = 0;
        this.CHOOSE_CONCERT = 1
        this.RESERVE_TICKET = 2
        this.PURCHASE_TICKET = 3
        this.DONE = 4
        this.NO_CONCERTS = 5;
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('choose-concert-form').addEventListener('submit', this.onSelectConcert);
        document.getElementById('reserve-ticket-button').addEventListener('click', this.onReserveTicket);
        document.getElementById('purchase-ticket-form').addEventListener('submit', this.onPurchaseTicket);
        this.dataStore.addChangeListener(this.onStateChange);
        // Start on the loading page
        this.dataStore.set("state", this.LOADING);

        this.client = new ConcertClient();
        // Get the concerts
        const concerts = await this.client.getConcerts();

        if (concerts && concerts.length > 0) {
            this.dataStore.set('concerts', concerts);
            this.dataStore.set("state", this.CHOOSE_CONCERT);
        } else if (concerts.length === 0) {
            this.dataStore.set("state", this.NO_CONCERTS);
            this.errorHandler("There are no concerts listed!  Come back after a concert has been created.");
        } else {
            this.errorHandler("Could not retrieve concerts!");
        }
    }

    /**
     * onStateChange - This gets called anytime the state changes in the dataStore.  This method hides/shows the
     * appropriate page for each state and calls the corresponding render method.
     */
    onStateChange() {
        const state = this.dataStore.get("state");

        const loadingSection = document.getElementById("loading")
        const chooseConcertSection = document.getElementById("choose-concert")
        const reserveTicketSection = document.getElementById("reserve-tickets")
        const purchaseTicketSection = document.getElementById("purchase-tickets")
        const doneSection = document.getElementById("done")
        const noConcertsSection = document.getElementById("no-concerts")

        if (state === this.LOADING) {
            loadingSection.classList.add("active")
            chooseConcertSection.classList.remove("active")
            reserveTicketSection.classList.remove("active")
            purchaseTicketSection.classList.remove("active")
            doneSection.classList.remove("active")
            noConcertsSection.classList.remove("active")
        } else if (state === this.CHOOSE_CONCERT) {
            loadingSection.classList.remove("active")
            chooseConcertSection.classList.add("active")
            reserveTicketSection.classList.remove("active")
            purchaseTicketSection.classList.remove("active")
            doneSection.classList.remove("active")
            noConcertsSection.classList.remove("active")
            this.renderConcertSelector();
        } else if (state === this.RESERVE_TICKET) {
            loadingSection.classList.remove("active")
            chooseConcertSection.classList.remove("active")
            reserveTicketSection.classList.add("active")
            purchaseTicketSection.classList.remove("active")
            doneSection.classList.remove("active")
            noConcertsSection.classList.remove("active")
            this.renderReservationPage();
        } else if (state === this.PURCHASE_TICKET) {
            loadingSection.classList.remove("active")
            chooseConcertSection.classList.remove("active")
            reserveTicketSection.classList.remove("active")
            purchaseTicketSection.classList.add("active")
            doneSection.classList.remove("active")
            noConcertsSection.classList.remove("active")
            this.renderPurchasePage();
        } else if (state === this.DONE) {
            loadingSection.classList.remove("active")
            chooseConcertSection.classList.remove("active")
            reserveTicketSection.classList.remove("active")
            purchaseTicketSection.classList.remove("active")
            doneSection.classList.add("active")
            noConcertsSection.classList.remove("active")
            this.renderDonePage();
        } else if (state === this.NO_CONCERTS) {
            loadingSection.classList.remove("active")
            chooseConcertSection.classList.remove("active")
            reserveTicketSection.classList.remove("active")
            purchaseTicketSection.classList.remove("active")
            doneSection.classList.remove("active")
            noConcertsSection.classList.add("active")
        }
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderConcertSelector() {
        let concertSelect = document.getElementById("choose-concert-input");

        const concerts = this.dataStore.get("concerts");

        let options = "";
        for (const concert of concerts) {
            options += `<option value="${concert.id}">${concert.name}</option>`
        }
        concertSelect.innerHTML = options;
    }

    async renderReservationPage() {
        const concert = this.dataStore.get("selectedConcert");

        document.getElementById("reserve-ticket-info").innerHTML = `
            <h3>${concert.name}</h3>
            <div>Date: ${concert.date}</div>
            <div>Asking Price: ${this.formatCurrency(concert.ticketBasePrice)}</div>
            <div>Would you like to reserve a ticket to purchase?</div>
        `;
    }

    async renderPurchasePage() {
        const concert = this.dataStore.get("selectedConcert");
        const ticketReservation = this.dataStore.get("ticketReservation");
        document.getElementById("purchase-price").min = concert.ticketBasePrice;
        document.getElementById("purchase-ticket-info").innerHTML = `
            <div>You have reserved a ticket to <strong>${concert.name}</strong>!</div>
            <div>Your reservation number is ${ticketReservation.ticketId}</div>
            <div>You have <strong>2 minutes</strong> to purchase the ticket before your reservation is released.</div>
            <div>The minimum ticket price is ${this.formatCurrency(concert.ticketBasePrice)}</div>
        `;
    }

    async renderDonePage() {
        const concert = this.dataStore.get("selectedConcert");
        const ticketReceipt = this.dataStore.get("ticketReceipt");
        document.getElementById("done-info").innerHTML = `
            <div>Thank you for your purchase!</div>
            <div>You have purchased one ticket to see <strong>${concert.name}</strong> for <strong>${this.formatCurrency(ticketReceipt.pricePaid)}</strong></div>
            <div>Your confirmation number is ${ticketReceipt.ticketId}</div>
        `;
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onSelectConcert(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let concertId = document.getElementById("choose-concert-input").value;

        // Get the object of the selected concert so we can store it.
        const concerts = this.dataStore.get("concerts");
        let selectedConcert = null;
        for (const concert of concerts) {
            if (concert.id === concertId) {
                selectedConcert = concert;
            }
        }

        if (selectedConcert) {
            this.dataStore.set("selectedConcert", selectedConcert);
            this.dataStore.set("state", this.RESERVE_TICKET)
        }
    }

    async onReserveTicket() {
        const concert = this.dataStore.get("selectedConcert");
        const ticketReservation = await this.client.reserveTicket(concert.id, this.errorHandler);

        if (ticketReservation && ticketReservation.ticketId) {
            this.dataStore.set("ticketReservation", ticketReservation);
            this.dataStore.set("state", this.PURCHASE_TICKET);
        } else {
            this.errorHandler("Error reserving ticket!  Try again...");
        }
    }

    async onPurchaseTicket(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        const ticketReservation = this.dataStore.get("ticketReservation");
        const pricePaid = parseInt(document.getElementById("purchase-price").value);
        const ticketReceipt = await this.client.purchaseTicket(ticketReservation.ticketId, pricePaid, this.errorHandler);

        if (ticketReceipt && ticketReceipt.ticketId) {
            this.dataStore.set("ticketReceipt", ticketReceipt);
            this.dataStore.set("state", this.DONE);
        } else {
            this.errorHandler("Error purchasing ticket! Try again...");
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const ticketPurchase = new TicketPurchase();
    ticketPurchase.mount();
};

window.addEventListener('DOMContentLoaded', main);
