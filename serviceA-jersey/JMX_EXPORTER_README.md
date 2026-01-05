# JMX Exporter Configuration for Prometheus

## Configuration JMX Exporter

Ce fichier configure le JMX Exporter pour exposer les métriques JVM à Prometheus.

## Utilisation

1. Téléchargez le JAR du JMX Exporter depuis :
   https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.20.0/jmx_prometheus_javaagent-0.20.0.jar

2. Placez le fichier `jmx_prometheus_javaagent-0.20.0.jar` dans le dossier `variantA-jersey/`

3. Lancez l'application avec l'agent JMX :
   ```bash
   java -javaagent:jmx_prometheus_javaagent-0.20.0.jar=8081:jmx-config.yml -jar target/variantA-jersey-1.0.0.jar
   ```

   Où :
   - `8081` est le port sur lequel les métriques Prometheus seront exposées
   - `jmx-config.yml` est le fichier de configuration

4. Les métriques seront disponibles sur : `http://localhost:8081/metrics`

## Configuration

Le fichier `jmx-config.yml` configure quelles métriques JMX exposer à Prometheus.

