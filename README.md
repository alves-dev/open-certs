<div align="center">
  <img src=".dev/logo-opencerts.svg" alt="OpenCerts Logo" width="300">
  <br><br>

  <h1>OpenCerts</h1>

  <p>
    <strong>Estude, Pratique e Evolua.</strong><br>
    Uma plataforma open-source e colaborativa para preparação de certificações Cloud.
  </p>

  <p>
    <a href="#-sobre">Sobre</a> •
    <a href="#-features">Funcionalidades</a> •
    <a href="#-tecnologias">Tecnologias</a> •
  </p>
</div>

---

## 🚀 Sobre

O **OpenCerts** é uma aplicação focada em desenvolvedores que buscam validação de conhecimento em nuvem (AWS, Azure, Google Cloud).

Diferente de plataformas complexas de quiz, o OpenCerts foca na **simplicidade** e na **repetição**. Com uma interface limpa, moderna e livre de distrações, você pode iniciar baterias de testes, salvar seu progresso através de *Session Identifiers* (sem necessidade de cadastro burocrático) e contribuir com novas questões para a comunidade.

## 📸 Screenshots

<div align="center">
  <img src="docs/images/dashboard-preview.png" alt="Dashboard" width="45%">
  <img src="docs/images/quiz-preview.png" alt="Tela de Quiz" width="45%">
</div>

## ✨ Features

- **🎯 Foco em Certificações:** Organizado por provedor (AWS, etc.) e níveis (Foundational, Associate, Professional).
- **💾 Session Identifiers:** Um sistema único de "Save Game". Defina um ID (ex: `minha-sessao-01`) e continue seus estudos de onde parou em qualquer dispositivo.
- **⚡ Feedback Imediato:** Validação visual instantânea de acertos e erros com explicações detalhadas.
- **🌙 Dark Mode Nativo:** Interface desenvolvida com base no tema *Midnight Slate* para conforto visual durante longas sessões de estudo.
- **📱 Responsivo:** Estude no desktop ou no mobile com uma experiência fluida.
- **🤝 Colaborativo:** Ferramenta integrada para submissão de novas questões pela comunidade.


## 🛠 Tecnologias

O projeto é construído sobre uma base sólida e moderna de Java:

- **Backend:** Java 25+, Spring Boot 3.x
- **Frontend:** Thymeleaf (Server-side rendering)
- **Estilização:** CSS3 Moderno (Custom Properties / CSS Variables)
- **Ícones:** SVG (Inline e Feather/Heroicons style)
- **Build:** gradle
- **Banco de Dados:** MongoDB (NoSQL)