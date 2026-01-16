
    <title>HAProxy Manager</title>
    <%@ include file="header.jsp" %>
    
    <div class="breadcrumbs">
        <div class="breadcrumbs-inner">
            <div class="row m-0">
                <div class="col-sm-8">
                    <div class="page-header float-left">
                        <div class="page-title">
                            <ol class="breadcrumb text-right">
                                <li class="active">Gestion HAProxy</li>
                            </ol>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

     <div class="content">
            <div class="animated fadeIn">
                <div class="row">
                    <div class="col-lg-10 offset-md-1">
                        <div class="card">
                            <div class="card-header">
                                <strong>Configuration serveur</strong> 
                            </div>
                            <div class="card-body">
                                    <div class="row"> 
                                        <div class="col-4">
                                            <!-- 
                                            <div class=" form-group">
                                                <label for="val" class="control-label mb-1">Adresse IP du WSL:</label>
                                                <input type="text" id="ipPrincipal" placeholder="IP du serveur principal" class="form-control" required>
                                            </div>
                                            -->
                                        </div>
                                    </div>
                            </div>
                            <div class="card-footer">
                                <button class="btn btn-primary btn-sm" id="ajouterServeur"><i class="fa fa-dot-circle-o"></i> Ajouter un serveur</button> 
                                <button class="btn btn-primary btn-sm" id="afficherServeurs"><i class="fa fa-dot-circle-o"></i> Valider changement</button> 
                            </div>
                        </div>
                    </div>
                    
                  <div id="serveurs" class="col-lg-12"></div>
    
                </div>
            </div>
        </div><!-- .animated -->
    </div><!-- .content -->

    

    <%@ include file="footer.jsp" %>
    <script>
        const ajouterServeurBtn = document.getElementById('ajouterServeur');
        const serveursDiv = document.getElementById('serveurs');

        ajouterServeurBtn.addEventListener('click', () => {
            const serveur = document.createElement('div');
            serveur.className='serveur col-lg-10 offset-md-1';
            serveur.innerHTML = `
                <div class="card">
                    <div class="card-header">
                        <strong class="card-title">Serveur front</strong>
                    </div>
                    <div class="card-footer">
                        <button class="btn btn-primary btn-sm ajouterServeurEnfant"><i class="fa fa-dot-circle-o"></i> Ajouter un serveur back</button> 
                        <button class="btn btn-primary btn-sm supprimerServeur"><i class="fa fa-dot-circle-o"></i> Supprimer</button> 
                    </div>
                    
                    <div class="card-body">
                        
                        <div class="row">
                        
                        <div class="col-4"> 
                            <div class="form-group">
                                <label for="order" class="form-control-label">Protocole:</label>
                                <select class="form-control">
                                    <option value="tcp">TCP</option>
                                    <option value="http">HTTP</option>
                                </select>
                            </div>
                        </div>
                        
                        <div class="col-4">
                            <div class=" form-group">
                                <label class="control-label mb-1">Nom serveur:</label>
                                <input type="text" class="form-control" placeholder="Nom" required>
                            </div>
                        </div>
                         
                        <div class="col-4">
                            <div class=" form-group">
                                <label class="control-label mb-1">Port:</label>
                                <input type="number" class="form-control" placeholder="Port" required>
                            </div>
                        </div>
                    </div>
                
                    <div class="serveursEnfants col-lg-6 offset-md-3"></div>
                
                    </div>
                </div>
            `;
            serveursDiv.appendChild(serveur);

            const ajouterServeurEnfantBtn = serveur.querySelector('.ajouterServeurEnfant');
            ajouterServeurEnfantBtn.addEventListener('click', () => {
                const serveurEnfant = document.createElement('div');
                serveurEnfant.className='row';
                serveurEnfant.innerHTML = `
                
                    <div class="col-6">
                        <div class=" form-group">
                            <input type="text" class="form-control" placeholder="IP du serveur enfant" value="0.0.0.0" required>
                        </div>
                    </div>
            
                    <div class="col-6">
                        <div class=" form-group">
                            <input type="number" class="form-control" placeholder="Port du serveur enfant" value="1" required>
                        </div>
                    </div>
            
                `;
                serveur.querySelector('.serveursEnfants').appendChild(serveurEnfant);
            });
            
            const supprimerServeurBtn = serveur.querySelector('.supprimerServeur');
            supprimerServeurBtn.addEventListener('click', () => {
                serveur.remove();
            });
        });
        
        function estUneAdresseIPv4Valide(adresseIP) {
            const regex = /^((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])$/;
            return regex.test(adresseIP);
        }
        
        function estUnPortValide(port) {
            return /^\d+$/.test(port) && port >= 1 && port <= 65535;
        }
        
        function afficherServeurs() {
            var canSend=true;
            const tousLesServeurs = document.querySelectorAll('.serveur');
            
            //const ipPrincipal = document.getElementById('ipPrincipal').value;
            const ipPrincipal = '1.1.1.1';
            
            if (!estUneAdresseIPv4Valide(ipPrincipal)) {
                canSend=false;
                alert("Veuillez entrer une adresse IP valide pour le WSL");
                return;
            }
            
            let donnéesPourServlet = [];

            tousLesServeurs.forEach(serveur => {
                const nom = serveur.querySelector('input[placeholder="Nom"]').value;
                const portPrincipal = serveur.querySelector('input[placeholder="Port"]').value;
                const protocole = serveur.querySelector('select').value;

                console.log('Port'+portPrincipal+'Protocole='+protocole);
                
                if(!estUnPortValide(portPrincipal)) {
                    canSend=false;
                    alert('Port invalide=' + portPrincipal);
                    return;
                }
                
                if (nom=== '') {
                    canSend=false;
                    alert('Nom de serveur vide');
                    console.log(nom);
                    return;
                }

                const serveursEnfants = serveur.querySelector('.serveursEnfants');
                const enfants = [];
                
                if (serveursEnfants.children.length > 0) {
                    console.log('  Serveurs enfants:');
                   
                    serveursEnfants.querySelectorAll('.row').forEach(row => {
                        const ipInput = row.querySelector('input[placeholder="IP du serveur enfant"]');
                        const portInput = row.querySelector('input[placeholder="Port du serveur enfant"]');

                        const ipEnfant = ipInput.value;
                        const portEnfant = portInput.value;

                        // Validation et ajout à la liste
                        if (estUneAdresseIPv4Valide(ipEnfant) && estUnPortValide(portEnfant)) {
                            enfants.push({ ip: ipEnfant, port: portEnfant });
                            console.log('IP=' + ipEnfant + ', Port=' + portEnfant);
                        } else {
                            canSend = false;
                            alert('Adresse IP invalide : ' + ipEnfant + ', Port=' + portEnfant);
                            return;
                        }
                    });
                    
                } else {
                    canSend=false;
                    alert('Le serveur ne contient pas de serveurs enfants');
                    return;
                }
                

                // Création d'un objet représentant le serveur principal et ses enfants
                const serveurPrincipal = {
                    ipPrincipal, 
                    protocole,
                    port: portPrincipal,
                    nom: nom,
                    enfants
                };

                donnéesPourServlet.push(serveurPrincipal);
        
        
            });
            
            console.log(JSON.stringify(donnéesPourServlet));
            
            // Si toutes les données sont valides, on envoie au servlet
            if (canSend && tousLesServeurs.length>0) {
                // Ici, vous devez implémenter l'appel au servlet en utilisant fetch ou une autre méthode
                // et passer les données en tant que corps de la requête (par exemple en JSON)
                fetch('ConfigurationServlet', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(donnéesPourServlet)
                })
                .then(response => response.json())
                .then(data => {
                    // Traiter la réponse du servlet
                    console.log(data);
                    alert(data.status);
                    // Vous pouvez par exemple afficher un message de succès à l'utilisateur
                })
                .catch(error => {
                    console.error('Erreur lors de l envoi des données au servlet:', error);
                    alert(error);
                    // Afficher un message d'erreur à l'utilisateur
                });
            }
    
    
        }

        const afficherServeursBtn = document.getElementById('afficherServeurs');
        afficherServeursBtn.addEventListener('click', afficherServeurs);
      
    </script>
