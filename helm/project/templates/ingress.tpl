{{- if .Values.ingress.enabled -}}
{{- $fullName := include "utils.fullname" . -}}
{{- $hasOAuth := index .Values "oauth2-proxy" "enabled" -}}
{{- $kubeVersion := include "utils.kubeVersion" . -}}

apiVersion: {{ include "utils.ingress.apiVersion" . }}
kind: Ingress
metadata:
  name: {{ $fullName }}
  labels:
{{ include "utils.labels" . | indent 4 }}
  annotations:
  {{- with .Values.ingress.annotations }}
    {{- toYaml . | nindent 4 }}
  {{- end }}
spec:
# Bind to HTTPS port with the given certificates
{{- if .Values.ingress.tls }}
  tls:
  {{- range .Values.ingress.tls }}
    - hosts:
      {{- range .hosts }}
        - {{ . | quote }}
      {{- end }}
      secretName: {{ .secretName }}
  {{- end }}
{{- end }}
  rules:
  # Bind the routing rules to each host
  {{- range .Values.ingress.hosts }}

    - host: {{ .host | quote }}
      http:
        paths:          
          - {{- $path := "/" -}}
            {{- $name := printf "%s-app" $fullName -}}            
            {{- include "utils.ingress.backend" (list . $path $name $kubeVersion) | indent 12 }}

          - {{- $path := "/oauth2" -}}
            {{- $name := printf "%s-oauth2-proxy" $fullName -}}            
            {{- include "utils.ingress.backend" (list . $path $name $kubeVersion) | indent 12 }}

  {{- end }}

{{- end }}