export class Offre {
    id!: string;                         // Unique identifier for the offer
    numtel!: string;                     // Contact number associated with the offer
    montant!: number;                    // Amount or cost associated with the offer
    delaisderealisation!: Date;          // Deadline for the realization of the offer
    entrepriseid!: string;               // ID of the company related to the offer
    documentdeproposition!: string;      // Proposal document, assumed to be a base64 encoded string
    datededepot!: Date;   
    appeloffreId!: string;      
    userid!: string;              // Date when the offer was deposited/submitted
}
