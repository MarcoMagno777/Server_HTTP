const elencoPersone= document.getElementById("elencoPersone");

const persone = [

    {nome: "Mario", cognome: "Rossi", eta: 30},
    {nome: "Giuseppe", cognome: "Verdi", eta: 40},
    {nome: "Filippo", cognome: "Bianchi", eta: 35},
    {nome: "Marta", cognome: "Neri", eta: 50},
    {nome: "Federica", cognome: "Meucci", eta: 28}

];

persone.forEach(persona => {

    const riga = document.createElement("tr");

    for(let x in persona){

        const colonna = document.createElement("th");
        colonna.innerHTML = persona[x];
        riga.append(colonna);

    }

    elencoPersone.append(riga);

});

