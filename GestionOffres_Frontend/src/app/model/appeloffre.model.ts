import { Categorie } from "./categorie.model";

export class AppelOffre {
    id!: string;                     // Unique identifier for the offer
    titre!: string;                  // Title of the offer
    description!: string;            // Description of the offer
    entrepriseId!: string; 
localisation!:string;          // ID of the company related to the offer
    datecreation!: Date;             // Date of creation of the offer
    datelimitesoumission!: Date;     // Deadline for submission
    img!: string;                    // Image for the offer, base64 encoded string
    document!: string;    
    categorie!: Categorie;           // PDF document for the offer, base64 encoded string
}