import { Entreprise } from './entreprise.model';

export class Notification {
  id!: string;
  entreprise!: Entreprise;
  id_appeloffre!: string;
  id_categorie!: string;
  message!:string;
  creationDate!: Date;
}
