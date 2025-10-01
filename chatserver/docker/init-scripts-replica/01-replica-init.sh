#!/bin/bash
set -e

# 데이터 디렉토리가 비어있을 때만 복제 초기화 실행
if [ -z "$(ls -A /var/lib/postgresql/data)" ]; then
    echo "Initializing PostgreSQL Replica from primary..."

    # 💡 PGPASSWORD 환경 변수를 설정하여 pg_basebackup이 비밀번호를 사용할 수 있도록 함
    export PGPASSWORD="${REPLICA_PASSWORD}"

    PGDATA_INIT=/var/lib/postgresql/data-init
    mkdir -p "$PGDATA_INIT"

    # 호스트 이름 수정: ms-postgres-source
    # --password 옵션은 필요 없어졌으므로 제거
    pg_basebackup -h postgres-source -D "$PGDATA_INIT" -U ${REPLICA_USER} -P --wal-method=stream

    # 복제 설정 파일 생성 (standby.signal은 pg_basebackup 시 자동으로 생성될 수 있음)
    echo "primary_conninfo = 'host=postgres-source port=5432 user=${REPLICA_USER} password=${REPLICA_PASSWORD}'" >> "$PGDATA_INIT/postgresql.conf"

    # 생성된 데이터로 기존 데이터 디렉토리를 덮어씀
    mv "$PGDATA_INIT"/* /var/lib/postgresql/data/
    rmdir "$PGDATA_INIT"
    echo "Replica initialization complete."
fi