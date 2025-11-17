DC = docker-compose -f docker-compose.yml

db/up:
	@echo "============= starting db local docker ============="
	$(DC) up database -d