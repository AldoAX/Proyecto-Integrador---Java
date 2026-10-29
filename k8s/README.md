# RetailOrder Service — Kubernetes (Docker Desktop)

Ejecuta los comandos desde la raíz del proyecto, `Proyecto-Integrador---Java`.
Si tu terminal está en la carpeta contenedora `retailservice`, primero ejecuta:

```sh
cd Proyecto-Integrador---Java
```

## Prerrequisitos
- Docker Desktop instalado con Kubernetes habilitado
  Settings > Kubernetes > Enable Kubernetes > Apply & Restart
- Verificar entorno antes de continuar:
  ```sh
  kubectl cluster-info
  kubectl config current-context   # debe mostrar: docker-desktop
  kubectl get nodes                # debe mostrar: docker-desktop Ready
  ```

## Paso 1 — Construir la imagen Docker
Desde la raíz del proyecto:

```sh
docker build -t retailorder-service:v1.0 .
docker images | findstr retailorder
```

NOTA: En Docker Desktop no es necesario hacer push.
La imagen está disponible en el clúster automáticamente.

## Paso 2 — Aplicar ConfigMap y Secret
```sh
kubectl apply -f k8s/config-and-secret.yaml
kubectl get configmap retailorder-config
kubectl get secret retailorder-secret
```

## Paso 3 — Aplicar el almacenamiento persistente
```sh
kubectl apply -f k8s/pvc.yaml
kubectl get pvc retailorder-pvc
```
Esperar que STATUS muestre: `Bound`.

## Paso 4 — Aplicar el Deployment
```sh
kubectl apply -f k8s/deployment.yaml
kubectl get pods -l app=retailorder
```
Esperar que los 3 pods muestren: `Running`.

## Paso 5 — Aplicar el Service
```sh
kubectl apply -f k8s/service.yaml
kubectl get svc retailorder-service
```
EXTERNAL-IP debe mostrar: `localhost`.

## Paso 6 — Verificar acceso
```sh
curl http://localhost/api/v1/productos
```

O abrir en el navegador: http://localhost/swagger-ui/index.html

## Comandos de diagnóstico
```sh
# Ver logs de todos los pods
kubectl logs -l app=retailorder --tail=50

# Ver logs en tiempo real
kubectl logs -l app=retailorder -f

# Inspeccionar un pod específico
kubectl describe pod <nombre-del-pod>

# Entrar al contenedor
kubectl exec -it <nombre-del-pod> -- sh

# Verificar variables de entorno inyectadas
kubectl exec -it <nombre-del-pod> -- env | grep -E "PORT|LOG_LEVEL|DB"

# Historial de versiones del deployment
kubectl rollout history deployment/retailorder-deployment

# Rollback a versión anterior
kubectl rollout undo deployment/retailorder-deployment
```

## Limpiar todo el entorno
```sh
kubectl delete -f k8s/
```
