{{- if .Values.app.enabled }}

apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "utils.fullname" . }}-app
  labels:
{{ include "utils.labels" . | indent 4 }}s
spec:
  replicas: {{ .Values.replicaCount }}
  selector:
    matchLabels:
      app.kubernetes.io/name: {{ include "utils.name" . }}-app
      app.kubernetes.io/instance: {{ .Release.Name }}
  template:
    metadata:
      labels:
        app.kubernetes.io/name: {{ include "utils.name" . }}-app
        app.kubernetes.io/instance: {{ .Release.Name }}
    spec:
    {{- with .Values.imagePullSecrets }}
      imagePullSecrets:
        {{- toYaml . | nindent 8 }}
    {{- end }}
      containers:
        - name: {{ .Chart.Name }}-app
          image: "{{ .Values.app.image.repository }}{{ if .Values.app.image.tag -}}:{{ .Values.app.image.tag }}{{- end }}"
          imagePullPolicy: {{ .Values.app.image.pullPolicy }}
          ports:
            - name: http
              containerPort: 80
              protocol: TCP
          resources:
            {{- toYaml .Values.resources | nindent 12 }}
      {{- with .Values.nodeSelector }}
      nodeSelector:
        {{- toYaml . | nindent 8 }}
      {{- end }}
    {{- with .Values.affinity }}
      affinity:
        {{- toYaml . | nindent 8 }}
    {{- end }}
    {{- with .Values.tolerations }}
      tolerations:
        {{- toYaml . | nindent 8 }}
    {{- end }}


---

apiVersion: v1
kind: Service
metadata:
  name: {{ include "utils.fullname" . }}-app
  labels:
{{ include "utils.labels" . | indent 4 }}-app
spec:
  type: {{ .Values.app.service.type }}
  ports:
    - port: {{ .Values.app.service.port }}
      targetPort: http
      protocol: TCP
      name: http
  selector:
    app.kubernetes.io/name: {{ include "utils.name" . }}-app
    app.kubernetes.io/instance: {{ .Release.Name }}


{{- end }}
