import BaseClass from '../util/baseClass';
import DataStore from '../util/DataStore';
import ConcertClient from "../api/concertClient";

/**
 * Logic needed for the create playlist page of the website.
 */
class ConcertAdmin extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onSubmit', 'onRefresh', 'renderConcerts'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch teh concert list.
     */
    mount() {
        document.getElementById('refresh').addEventListener('click', this.onRefresh);
        document.getElementById('create-playlist-form').addEventListener('submit', this.onSubmit);

        this.client = new ConcertClient();
        this.dataStore.addChangeListener(this.renderConcerts)
        this.fetchConcerts();
    }

    async fetchConcerts() {
        const concerts = await this.client.getConcerts(this.errorHandler)

        if (concerts && concerts.length > 0) {
            for (const concert of concerts) {
                concert.reservations = await this.fetchReservations(concert.id);
                concert.purchases = await this.fetchPurchases(concert.id);
            }
        }
        this.dataStore.set("concerts", concerts);
    }

    async fetchReservations(concertId) {
        return await this.client.getReservedTicketsForConcert(concertId, this.errorHandler);
    }

    async fetchPurchases(concertId) {
        return await this.client.getPurchasedTicketsForConcert(concertId, this.errorHandler);
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    renderConcerts() {
        let concertHtml = "";
        const concerts = this.dataStore.get("concerts");

        if (concerts) {
            for (const concert of concerts) {
                concertHtml += `
                    <div class="card">
                        <h2>${concert.name}</h2>
                        <div>Date: ${concert.date}</div>
                        <div>Base Price: ${this.formatCurrency(concert.ticketBasePrice)}</div>
                        <p>
                            <h3>Ticket Reservations</h3>
                            <ul>
                `;
                if (concert.reservations && concert.reservations.length > 0) {
                    for (const reservation of concert.reservations) {
                        concertHtml += `
                                <li>
                                    <div>Ticket ID: ${reservation.ticketId}</div>
                                    <div>Date Reserved: ${reservation.dateOfReservation}</div>
                                    <div>Reservation Closed: ${reservation.reservationClosed}</div>
                                    <div>Date Reservation Closed: ${reservation.dateReservationClosed}</div>
                                    <div>Ticket Purchased: ${reservation.purchasedTicket}</div>
                                </li>
                        `;
                    }
                } else {
                    concertHtml += `
                                <li>No Ticket Reservations.</li>
                    `;
                }
                concertHtml += `
                            </ul>
                        </p>
                        <p>
                            <h3>Ticket Purchases</h3>
                            <ul>
                `;
                if (concert.purchases && concert.purchases.length > 0) {
                    for (const purchase of concert.purchases) {
                        concertHtml += `
                                <li>
                                    <div>Ticket ID: ${purchase.ticketId}</div>
                                    <div>Date Purchased: ${purchase.dateOfPurchase}</div>
                                    <div>Price Paid: ${purchase.pricePaid}</div>
                                </li>
                        `;
                    }
                } else {
                    concertHtml += `
                                <li>No Ticket Purchases.</li>
                    `;
                }
                concertHtml += `
                            </ul>
                        </p>
                    </div>`;
            }
        } else {
            concertHtml = `<div>There are no concerts...</div>`;
        }

        document.getElementById("concert-list").innerHTML = concertHtml;
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    onRefresh() {
        this.fetchConcerts();
    }

    /**
     * Method to run when the create playlist submit button is pressed. Call the MusicPlaylistService to create the
     * playlist.
     */
    async onSubmit(event) {
        // Prevent the form from refreshing the page
        event.preventDefault();

        // Set the loading flag
        let createButton = document.getElementById('create-concert');
        createButton.innerText = 'Loading...';
        createButton.disabled = true;

        // Get the values from the form inputs
        const concertName = document.getElementById('concert-name').value;
        const date = document.getElementById('date').value;
        const baseTicketPrice = document.getElementById('ticket-price').value;

        // Create the concert
        const concert = await this.client.createConcert(concertName, date, baseTicketPrice);

        // Reset the form
        document.getElementById("create-playlist-form").reset();

        // Re-enable the form
        createButton.innerText = 'Create';
        createButton.disabled = false;
        this.onRefresh();
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const concertAdmin = new ConcertAdmin();
    concertAdmin.mount();
};

window.addEventListener('DOMContentLoaded', main);
