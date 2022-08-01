import BaseClass from "../util/baseClass";
import axios from 'axios'

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class ConcertClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getConcerts', 'getConcert', 'getReservedTicketsForConcert', 'getPurchasedTicketsForConcert', 'reserveTicket', 'purchaseTicket'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    /**
     * Get all concerts
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns an array of concerts
     */
    async getConcerts(errorCallback) {
        try {
            const response = await this.client.get(`/concerts`);
            return response.data;
        } catch(error) {
            this.handleError("getConcerts", error, errorCallback);
        }
    }

    /**
     * Gets the concert for the given ID.
     * @param id Unique identifier for a concert
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The concert
     */
    async getConcert(id, errorCallback) {
        try {
            const response = await this.client.get(`/concerts/${id}`);
            return response.data.concert;
        } catch (error) {
            this.handleError("getConcert", error, errorCallback)
        }
    }

    async createConcert(name, date, ticketBasePrice, errorCallback) {
        try {
            const response = await this.client.post(`concerts`, {
                name: name,
                date: date,
                ticketBasePrice: ticketBasePrice
            });
            return response.data;
        } catch (error) {
            this.handleError("createConcert", error, errorCallback);
        }
    }

    /**
     *
     * @param concertId
     * @param errorCallback
     * @returns {Promise<*>}
     */
    async getReservedTicketsForConcert(concertId, errorCallback) {
        try {
            const response = await this.client.get(`reservedtickets/concerts/${concertId}`);
            return response.data;
        } catch (error) {
            this.handleError("getReservedTicketForConcert", error, errorCallback);
        }
    }

    /**
     *
     * @param concertId
     * @param errorCallback
     * @returns {Promise<*>}
     */
    async getPurchasedTicketsForConcert(concertId, errorCallback) {
        try {
            const response = await this.client.get(`purchasedtickets/concerts/${concertId}`);
            return response.data;
        } catch (error) {
            this.handleError("getPurchasedTicketsForConcert", error, errorCallback);
        }
    }

    /**
     * Create a new playlist.
     * @param name The name of the playlist to create.
     * @param customerId The user who is the owner of the playlist.
     * @param tags Metadata tags to associate with a playlist.
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The playlist that has been created.
     */
    async reserveTicket(concertId, errorCallback) {
        try {
            const response = await this.client.post(`reservedtickets`, {
                concertId: concertId,
            });
            return response.data;
        } catch (error) {
            this.handleError("reserveTicket", error, errorCallback);
        }
    }

    /**
     * Add a song to a playlist.
     * @param id The id of the playlist to add a song to.
     * @param asin The asin that uniquely identifies the album.
     * @param trackNumber The track number of the song on the album.
     * @returns The list of songs on a playlist.
     */
    async purchaseTicket(ticketId, pricePaid, errorCallback) {
        try {
            const response = await this.client.post(`purchasedtickets`, {
                ticketId: ticketId,
                pricePaid: pricePaid
            });
            return response.data;
        } catch (error) {
            this.handleError("purchaseTicket", error, errorCallback);
        }
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}
